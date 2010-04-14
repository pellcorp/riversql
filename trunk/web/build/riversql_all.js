/**
*
*  Base64 encode / decode
*  http://www.webtoolkit.info/
*
**/

var Base64 = (function() {

    // private property
    var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // private method for UTF-8 encoding
    function utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    }

    // public method for encoding
    return {
        encode : (typeof btoa == 'function') ? function(input) { return btoa(input); } : function (input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = utf8Encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                keyStr.charAt(enc1) + keyStr.charAt(enc2) +
                keyStr.charAt(enc3) + keyStr.charAt(enc4);
            }
            return output;
        }
    };
})();


Ext.override(Ext.grid.GridPanel, {
    getExcelXml: function(includeHidden) {
        var worksheet = this.createWorksheet(includeHidden);
        var totalWidth = this.getColumnModel().getTotalWidth(includeHidden);
        return '<?xml version="1.0" encoding="utf-8"?>' +
            '<ss:Workbook xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:o="urn:schemas-microsoft-com:office:office">' +
            '<o:DocumentProperties><o:Title>' + this.title + '</o:Title></o:DocumentProperties>' +
            '<ss:ExcelWorkbook>' +
                '<ss:WindowHeight>' + worksheet.height + '</ss:WindowHeight>' +
                '<ss:WindowWidth>' + worksheet.width + '</ss:WindowWidth>' +
                '<ss:ProtectStructure>False</ss:ProtectStructure>' +
                '<ss:ProtectWindows>False</ss:ProtectWindows>' +
            '</ss:ExcelWorkbook>' +
            '<ss:Styles>' +
                '<ss:Style ss:ID="Default">' +
                    '<ss:Alignment ss:Vertical="Top" ss:WrapText="1" />' +
                    '<ss:Font ss:FontName="arial" ss:Size="10" />' +
                    '<ss:Borders>' +
                        '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />' +
                        '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />' +
                        '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />' +
                        '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />' +
                    '</ss:Borders>' +
                    '<ss:Interior />' +
                    '<ss:NumberFormat />' +
                    '<ss:Protection />' +
                '</ss:Style>' +
                '<ss:Style ss:ID="title">' +
                    '<ss:Borders />' +
                    '<ss:Font />' +
                    '<ss:Alignment ss:WrapText="1" ss:Vertical="Center" ss:Horizontal="Center" />' +
                    '<ss:NumberFormat ss:Format="@" />' +
                '</ss:Style>' +
                '<ss:Style ss:ID="headercell">' +
                    '<ss:Font ss:Bold="1" ss:Size="10" />' +
                    '<ss:Alignment ss:WrapText="1" ss:Horizontal="Center" />' +
                    '<ss:Interior ss:Pattern="Solid" ss:Color="#A3C9F1" />' +
                '</ss:Style>' +
                '<ss:Style ss:ID="even">' +
                    '<ss:Interior ss:Pattern="Solid" ss:Color="#CCFFFF" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="even" ss:ID="evendate">' +
                    '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="even" ss:ID="evenint">' +
                    '<ss:NumberFormat ss:Format="0" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="even" ss:ID="evenfloat">' +
                    '<ss:NumberFormat ss:Format="0.00" />' +
                '</ss:Style>' +
                '<ss:Style ss:ID="odd">' +
                    '<ss:Interior ss:Pattern="Solid" ss:Color="#CCCCFF" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="odd" ss:ID="odddate">' +
                    '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="odd" ss:ID="oddint">' +
                    '<ss:NumberFormat ss:Format="0" />' +
                '</ss:Style>' +
                '<ss:Style ss:Parent="odd" ss:ID="oddfloat">' +
                    '<ss:NumberFormat ss:Format="0.00" />' +
                '</ss:Style>' +
            '</ss:Styles>' +
            worksheet.xml +
            '</ss:Workbook>';
    },

    createWorksheet: function(includeHidden) {

//      Calculate cell data types and extra class names which affect formatting
        var cellType = [];
        var cellTypeClass = [];
        var cm = this.getColumnModel();
        var totalWidthInPixels = 0;
        var colXml = '';
        var headerXml = '';
        for (var i = 0; i < cm.getColumnCount(); i++) {
            if (includeHidden || !cm.isHidden(i)) {
                var w = cm.getColumnWidth(i)
                totalWidthInPixels += w;
                colXml += '<ss:Column ss:AutoFitWidth="1" ss:Width="' + w + '" />';
                headerXml += '<ss:Cell ss:StyleID="headercell">' +
                    '<ss:Data ss:Type="String">' + cm.getColumnHeader(i) + '</ss:Data>' +
                    '<ss:NamedCell ss:Name="Print_Titles" /></ss:Cell>';
                //var fld = this.store.recordType.prototype.fields.get(cm.getDataIndex(i));
                cellType.push("String");
                cellTypeClass.push("");
//                switch(fld.type) {
//                    case "int":
//                        cellType.push("Number");
//                        cellTypeClass.push("int");
//                        break;
//                    case "float":
//                        cellType.push("Number");
//                        cellTypeClass.push("float");
//                        break;
//                    case "bool":
//                    case "boolean":
//                        cellType.push("String");
//                        cellTypeClass.push("");
//                        break;
//                    case "date":
//                        cellType.push("DateTime");
//                        cellTypeClass.push("date");
//                        break;
//                    default:
//                        cellType.push("String");
//                        cellTypeClass.push("");
//                        break;
//                }
            }
        }
        var visibleColumnCount = cellType.length;

        var result = {
            height: 9000,
            width: Math.floor(totalWidthInPixels * 30) + 50
        };

//      Generate worksheet header details.
        this.title='SQL Query Export'
        var t = '<ss:Worksheet ss:Name="' + this.title + '">' +
            '<ss:Names>' +
                '<ss:NamedRange ss:Name="Print_Titles" ss:RefersTo="=\'' + this.title + '\'!R1:R2" />' +
            '</ss:Names>' +
            '<ss:Table x:FullRows="1" x:FullColumns="1"' +
                ' ss:ExpandedColumnCount="' + visibleColumnCount +
                '" ss:ExpandedRowCount="' + (this.store.getCount() + 2) + '">' +
                colXml +
                '<ss:Row ss:Height="38">' +
                    '<ss:Cell ss:StyleID="title" ss:MergeAcross="' + (visibleColumnCount - 1) + '">' +
                      '<ss:Data xmlns:html="http://www.w3.org/TR/REC-html40" ss:Type="String">' +
                        '<html:B><html:U><html:Font html:Size="15">' + this.title +
                        '</html:Font></html:U></html:B>Generated by riversql</ss:Data><ss:NamedCell ss:Name="Print_Titles" />' +
                    '</ss:Cell>' +
                '</ss:Row>' +
                '<ss:Row ss:AutoFitHeight="1">' +
                headerXml + 
                '</ss:Row>';

//      Generate the data rows from the data in the Store
        for (var i = 0, it = this.store.data.items, l = it.length; i < l; i++) {
            t += '<ss:Row>';
            var cellClass = (i & 1) ? 'odd' : 'even';
            r = it[i].data;
            var k = 0;
            for (var j = 0; j < cm.getColumnCount(); j++) {
                if (includeHidden || !cm.isHidden(j)) {
                    var v = r[cm.getDataIndex(j)];
                    t += '<ss:Cell ss:StyleID="' + cellClass + cellTypeClass[k] + '"><ss:Data ss:Type="' + cellType[k] + '">';
                        if (cellType[k] == 'DateTime') {
                            t += v.format('Y-m-d');
                        } else {
                            t += v;
                        }
                    t +='</ss:Data></ss:Cell>';
                    k++;
                }
            }
            t += '</ss:Row>';
        }

        result.xml = t + '</ss:Table>' +
            '<x:WorksheetOptions>' +
                '<x:PageSetup>' +
                    '<x:Layout x:CenterHorizontal="1" x:Orientation="Landscape" />' +
                    '<x:Footer x:Data="Page &amp;P of &amp;N" x:Margin="0.5" />' +
                    '<x:PageMargins x:Top="0.5" x:Right="0.5" x:Left="0.5" x:Bottom="0.8" />' +
                '</x:PageSetup>' +
                '<x:FitToPage />' +
                '<x:Print>' +
                    '<x:PrintErrors>Blank</x:PrintErrors>' +
                    '<x:FitWidth>1</x:FitWidth>' +
                    '<x:FitHeight>32767</x:FitHeight>' +
                    '<x:ValidPrinterInfo />' +
                    '<x:VerticalResolution>600</x:VerticalResolution>' +
                '</x:Print>' +
                '<x:Selected />' +
                '<x:DoNotDisplayGridlines />' +
                '<x:ProtectObjects>False</x:ProtectObjects>' +
                '<x:ProtectScenarios>False</x:ProtectScenarios>' +
            '</x:WorksheetOptions>' +
        '</ss:Worksheet>';
        return result;
    }
});


var xsmargin=-5;
var xemargin=10;
var ysmargin=-5
var yemargin=17;

draw2d.WindowFigure.ZOrderIndex=5000;

TableConnectionDecorator=function(){
	this.setBackgroundColor(new draw2d.Color(255,255,255));
};
TableConnectionDecorator.prototype=new draw2d.ConnectionDecorator;
TableConnectionDecorator.prototype.type="TableConnectionDecorator";
TableConnectionDecorator.prototype.paint=function(g){
	if(this.backgroundColor!=null){
		g.setColor(this.backgroundColor);
		g.fillPolygon([3,20,20,3],[0,8,-8,0]);
	}
	g.setColor(this.color);
	g.setStroke(1);
	g.drawPolygon([3,20,20,3],[0,8,-8,0]);
};

TableConnection=function(message){
	
	draw2d.Connection.call(this);
	
	this.setTargetDecorator(new TableConnectionDecorator());
	this.setSourceAnchor(new draw2d.ChopboxConnectionAnchor());
	this.setTargetAnchor(new draw2d.ChopboxConnectionAnchor());
	this.setRouter(new draw2d.ManhattanConnectionRouter());
	var label = new draw2d.Label(message);
	label.setBackgroundColor(new draw2d.Color(255,255,255));
	label.setBorder(new draw2d.LineBorder(1));
	
	
	var mml=new draw2d.ManhattanMidpointLocator(this);
	
	this.addFigure(label, mml);
};

TableConnection.prototype=new draw2d.Connection();

TableConnection.prototype.addFigure=function(/*:draw2d.Figure*/ figure, /*:draw2d.ConnectionLocator*/ locator)
{
	
  var entry = new Object();
  entry.figure  = figure;
  entry.locator = locator;

  this.children.add(entry);
  if(this.graphics !=null)
    this.paint();

  var oThis = this;
  var mouseDown = function()
  {
    var oEvent = arguments[0] || window.event;
    oEvent.returnValue = false;
    oThis.getWorkflow().setCurrentSelection(oThis);
    oThis.getWorkflow().showLineResizeHandles(oThis);
  }
  if (figure.getHTMLElement().addEventListener){
	  figure.getHTMLElement().addEventListener("mousedown", mouseDown, false);
  }
  else if (figure.getHTMLElement().attachEvent){
	  figure.getHTMLElement().attachEvent("onmousedown", mouseDown);
  }
}

TablePort=function(){
	var circle=new draw2d.Circle();
	
	draw2d.Port.call(this,circle);
	this.setDimension(10,10);
	this.setBackgroundColor(new draw2d.Color(190,190,255));
};

TablePort.prototype=new draw2d.Port;TablePort.prototype.type="TablePort";
TablePort.prototype.onDrop=function(port){
	if(this.parentNode.id==port.parentNode.id){
		
	}else{
		obj=this;
		obj.port_=port;
		function showResultText(btn,text){
			var command=new draw2d.CommandConnect(obj.parentNode.workflow,obj,obj.port_);
			var tc=new TableConnection(text);
			command.setConnection(tc);
			obj.parentNode.workflow.getCommandStack().execute(command);
			//alert(tc.lineSegments.getSize());
		}
		Ext.MessageBox.prompt('Name', 'Please enter the name:', showResultText);

	}
};


TableConnection.prototype.isResizeable=function()
{
  return false;
}



TableConnection.prototype.paint=function(){
	for(var i=0;i<this.children.getSize();i++){
		var entry=this.children.get(i);
		if(entry.isAppended==true){this.html.removeChild(entry.figure.getHTMLElement());}
		entry.isAppended=false;
	}
	if(this.graphics==null){
		this.graphics=new jsGraphics(this.id);
	}else{this.graphics.clear();}
	this.graphics.setStroke(this.stroke);
	this.graphics.setColor(this.lineColor.getHTMLStyle());
	this.startStroke();
	this.router.route(this);
	//if(this.getSource().getParent().isMoving==false&&this.getTarget().getParent().isMoving==false){
		if(this.targetDecorator!=null){
			this.targetDecorator.paint(new draw2d.Graphics(this.graphics,this.getEndAngle(),this.getEndPoint()));
		}
		if(this.sourceDecorator!=null){
			this.sourceDecorator.paint(new draw2d.Graphics(this.graphics,this.getStartAngle(),this.getStartPoint()));
		}
	//}
	this.finishStroke();
	for(var i=0;i<this.children.getSize();i++){
		var entry=this.children.get(i);
		this.html.appendChild(entry.figure.getHTMLElement());
		entry.isAppended=true;entry.locator.relocate(entry.figure);
	}
};

TableWorkflow=function(element){
	draw2d.Workflow.call(this,element);
	this.addremoveListeners=new draw2d.ArrayList();
}
TableWorkflow.prototype = new draw2d.Workflow;


TableWorkflow.prototype.clear=function(){
	this.scrollTo(0,0,true);
	this.gridWidthX=10;
	this.gridWidthY=10;
	this.snapToGridHelper=null;
	this.verticalSnapToHelperLine=null;
	this.horizontalSnapToHelperLine=null;
	var _4f5b=this.getDocument();
	var _4f5c=_4f5b.getLines().clone();
	for(var i=0;i<_4f5c.getSize();i++){new draw2d.CommandDelete(_4f5c.get(i)).execute();}
	var _4f5e=this.getFigures().clone();
	for(var i=0;i<_4f5e.getSize();i++){new TableCommandDelete(_4f5e.get(i)).execute();}
	this.commonPorts.removeAllElements();
	this.dropTargets.removeAllElements();
	this.compartments.removeAllElements();
	this.selectionListeners.removeAllElements();
	this.dialogs.removeAllElements();
	this.commandStack=new draw2d.CommandStack();
	this.currentSelection=null;
	this.currentMenu=null;
	draw2d.Drag.clearCurrent();
	this.addremoveListeners.removeAllElements();
}

TableWorkflow.prototype.addAddRemoveListener=function(w){if(w!=null){this.addremoveListeners.add(w);}};
TableWorkflow.prototype.removeAddRemoveListener=function(w){this.addremoveListeners.remove(w);};

TableWorkflow.prototype.addFigure=function(figure,xPos,yPos){
	draw2d.Workflow.prototype.addFigure.call(this,figure,xPos,yPos);
	for(var i=0;i<this.addremoveListeners.getSize();i++){
		var w=this.addremoveListeners.get(i);
		if(w.onAdded){w.onAdded(figure);}
	}
}

TableWorkflow.prototype.removeFigure=function(figure){
	draw2d.Workflow.prototype.removeFigure.call(this,figure);
	for(var i=0;i<this.addremoveListeners.getSize();i++){
		var w=this.addremoveListeners.get(i);
		if(w.onRemoved){w.onRemoved(figure);}
	}
}


/** @private **/
TableWorkflow.prototype.type="TableWorkflow";

TableWorkflow.prototype.showResizeHandles=function(_4fb6){
	this.hideLineResizeHandles();
	this.hideResizeHandles();
	if(this.getEnableSmoothFigureHandling()==true&&this.getCurrentSelection()!=_4fb6){
		this.resizeHandle1.setAlpha(0.01);
		this.resizeHandle2.setAlpha(0.01);
		this.resizeHandle3.setAlpha(0.01);
		this.resizeHandle4.setAlpha(0.01);
		this.resizeHandle5.setAlpha(0.01);
		this.resizeHandle6.setAlpha(0.01);
		this.resizeHandle7.setAlpha(0.01);
		this.resizeHandle8.setAlpha(0.01);
	}
	var width=this.resizeHandle1.getWidth();
	var height=this.resizeHandle1.getHeight();
	var _4fb9=_4fb6.getHeight()+yemargin;
	var _4fba=_4fb6.getWidth()+xemargin;
	var xPos=_4fb6.getX()+xsmargin;
	var yPos=_4fb6.getY()+ysmargin;
	draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle1,xPos-width,yPos-height);
	draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle3,xPos+_4fba,yPos-height);
	draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle5,xPos+_4fba,yPos+_4fb9);
	draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle7,xPos-width,yPos+_4fb9);
	this.moveFront(this.resizeHandle1);
	this.moveFront(this.resizeHandle3);
	this.moveFront(this.resizeHandle5);
	this.moveFront(this.resizeHandle7);
	this.resizeHandle1.setCanDrag(_4fb6.isResizeable());
	this.resizeHandle3.setCanDrag(_4fb6.isResizeable());
	this.resizeHandle5.setCanDrag(_4fb6.isResizeable());
	this.resizeHandle7.setCanDrag(_4fb6.isResizeable());
	if(_4fb6.isResizeable()){
		var green=new draw2d.Color(0,255,0);
		this.resizeHandle1.setBackgroundColor(green);
		this.resizeHandle3.setBackgroundColor(green);
		this.resizeHandle5.setBackgroundColor(green);
		this.resizeHandle7.setBackgroundColor(green);
	}else{
		this.resizeHandle1.setBackgroundColor(null);
		this.resizeHandle3.setBackgroundColor(null);
		this.resizeHandle5.setBackgroundColor(null);
		this.resizeHandle7.setBackgroundColor(null);
	}
	if(_4fb6.isStrechable()&&_4fb6.isResizeable()){
		this.resizeHandle2.setCanDrag(_4fb6.isResizeable());
		this.resizeHandle4.setCanDrag(_4fb6.isResizeable());
		this.resizeHandle6.setCanDrag(_4fb6.isResizeable());
		this.resizeHandle8.setCanDrag(_4fb6.isResizeable());
		draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle2,xPos+(_4fba/2)-this.resizeHandleHalfWidth,yPos-height);
		draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle4,xPos+_4fba,yPos+(_4fb9/2)-(height/2));
		draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle6,xPos+(_4fba/2)-this.resizeHandleHalfWidth,yPos+_4fb9);
		draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle8,xPos-width,yPos+(_4fb9/2)-(height/2));
		this.moveFront(this.resizeHandle2);
		this.moveFront(this.resizeHandle4);
		this.moveFront(this.resizeHandle6);this.moveFront(this.resizeHandle8);
	}
};

TableWorkflow.prototype.moveResizeHandles=function(_4fbe){
	var _4fbf=this.resizeHandle1.getWidth();
	var _4fc0=this.resizeHandle1.getHeight();
	var _4fc1=_4fbe.getHeight()+yemargin;
	var _4fc2=_4fbe.getWidth()+xemargin;
	var xPos=_4fbe.getX()+xsmargin;
	var yPos=_4fbe.getY()+ysmargin;
	this.resizeHandle1.setPosition(xPos-_4fbf,yPos-_4fc0);
	this.resizeHandle3.setPosition(xPos+_4fc2,yPos-_4fc0);
	this.resizeHandle5.setPosition(xPos+_4fc2,yPos+_4fc1);
	this.resizeHandle7.setPosition(xPos-_4fbf,yPos+_4fc1);
	if(_4fbe.isStrechable()){
		this.resizeHandle2.setPosition(xPos+(_4fc2/2)-this.resizeHandleHalfWidth,yPos-_4fc0);
		this.resizeHandle4.setPosition(xPos+_4fc2,yPos+(_4fc1/2)-(_4fc0/2));
		this.resizeHandle6.setPosition(xPos+(_4fc2/2)-this.resizeHandleHalfWidth,yPos+_4fc1);
		this.resizeHandle8.setPosition(xPos-_4fbf,yPos+(_4fc1/2)-(_4fc0/2));
	}
};

TableWorkflow.prototype.loadTables=function(tables){
	for ( var j = 0; j < tables.length; j++) {
		var x=tables[j].x;
		var y=tables[j].y;
		var width=tables[j].width;
		var height=tables[j].height;
		var name=tables[j].name;
		
		var tableFigure = new TableFigure(name);
		this.addFigure(tableFigure, x, y);
		tableFigure.addColumns(tables[j].columns);
		tableFigure.setDimension(width,height);
		if(tables[j].open==false){
			tableFigure.collapse();
		}
	}
}
TableWorkflow.prototype.loadConnections=function(connections){
	for ( var j = 0; j < connections.length; j++) {
		var source=connections[j].source;
		var target=connections[j].target;
		var sourceTable=null;
		var targetTable=null;
		for(var i=0; i<this.figures.getSize();i++){
		     var figure = this.figures.get(i);
		     if(figure.title==source){
		        sourceTable=figure;
		        break;
		     }
		}
		for(var i=0; i<this.figures.getSize();i++){
		     var figure = this.figures.get(i);
		     if(figure.title==target){
		        targetTable=figure;
		        break;
		     }
		}
		if(sourceTable==null|| targetTable==null)
			continue;
		var tc=new TableConnection("A");
		tc.setSource(sourceTable.portTop);
		tc.setTarget(targetTable.portBottom);
		
		this.addFigure(tc);
	}
	
}

TableFigure=function(/*:String*/ title)
{
  draw2d.WindowFigure.call(this,title);
  //this.setDimension(75,400);
  this.children = new Object();
  this.length=0;
  this.setZOrder(TableFigure.ZOrderIndex);
  this.ports = new draw2d.ArrayList();
  this.setDeleteable(true);
  this.open=true;
  this.resizeable=true;

}

TableFigure.ZOrderBaseIndex = 100;
TableFigure.setZOrderBaseIndex=function(/*:int*/ index)
{
  draw2d.WindowFigure.ZOrderBaseIndex = index;
}

TableFigure.prototype = new draw2d.WindowFigure;
/** @private **/
TableFigure.prototype.type="TableFigure";


TableFigure.prototype.isSelectable=function()
{
  return true;
}

TableFigure.prototype.isResizeable=function()
{
  return this.resizeable;
}

/**
 * @private
 **/
TableFigure.prototype.dispose=function()
{
	 draw2d.Figure.prototype.dispose.call(this);
	for(var i=0;i<this.ports.getSize();i++)
	  {
	     this.ports.get(i).dispose();
	  }

	this.ports = null;
	this.children=null;
  //draw2d.WindowFigure.prototype.dispose.call(this);
 
}


TableFigure.prototype.paint=function()
{
  draw2d.WindowFigure.prototype.paint.call(this);

  for(var i=0;i<this.ports.getSize();i++)
  {
     this.ports.get(i).paint();
  }
}


TableFigure.prototype.getPorts=function()
{
  return this.ports;
}

TableFigure.prototype.getPort= function(/*:String*/ portName)
{
  if(this.ports==null)
    return null;
  for(var i=0;i<this.ports.getSize();i++)
  {
   var port = this.ports.get(i);
   if(port.getName() == portName)
      return port;
  }
}


TableFigure.prototype.addPort=function(/*:draw2d.Port*/ port, /*:int*/ x, /*:int*/y)
{
  this.ports.add(port);
  port.setOrigin(x,y);
  port.setPosition(x,y);
  port.setParent(this);
  // You can't delete a port with the [DEL] key if a port is a child of a node
  port.setDeleteable(false);

  this.html.appendChild(port.getHTMLElement());
  if(this.workflow!=null)
  {
    this.workflow.registerPort(port);
  }
}
TableFigure.prototype.removePort=function(/*:draw2d.Port*/ port)
{
  if(this.ports!=null)
    this.ports.removeElementAt(this.ports.indexOf(port));
  try
  {
    this.html.removeChild(port.getHTMLElement());
  }
  catch(exc)
  {
  }
  if(this.workflow!=null)
    this.workflow.unregisterPort(port);
}

TableFigure.prototype.setWorkflow= function(/*:draw2d.Workflow*/ workflow)
{
  var oldWorkflow = this.workflow;
  draw2d.WindowFigure.prototype.setWorkflow.call(this,workflow);

  if(oldWorkflow!=null)
  {
      for(var i=0;i<this.ports.getSize();i++)
      {
         oldWorkflow.unregisterPort(this.ports.get(i));
      }
  }

 
 
	if(workflow!=null&& this.portTop==null){
		this.portTop=new TablePort();
		this.portTop.setWorkflow(workflow);
		this.addPort(this.portTop,0,0);
		this.portRight=new TablePort();
		this.portRight.setWorkflow(workflow);
		this.addPort(this.portRight,0,0);
		this.portBottom=new TablePort();
		this.portBottom.setWorkflow(workflow);
		this.addPort(this.portBottom,0,0);
		this.portLeft=new TablePort();
		this.portLeft.setWorkflow(workflow);
		this.addPort(this.portLeft,0,0);
		this.recalculateSize();
	}
}


TableFigure.prototype.recalculateSize=function(name){
	this.setDimension(this.getWidth(),this.getHeight());
	
};

TableFigure.prototype.refreshConnections=function()
{
   // notify the view that the element has been changed
   if(this.workflow!=null)
      this.workflow.refreshConnections(this);
}


/**
 * @private
 **/
TableFigure.prototype.createHTMLElement=function()
{
//  var item = draw2d.WindowFigure.prototype.createHTMLElement.call(this);
  var item=draw2d.Figure.prototype.createHTMLElement.call(this);
  //item.className="x-window x-resizable-pinned";
  item.className="x-window";
  item.style.display="block";
  this.titlebar=document.createElement("div");
  
  this.titlebar.className="x-window-tl";
  tr=document.createElement("div");
  tr.style.overflow="hidden";
  tr.className="x-window-tr";
  tc=document.createElement("div");
  tc.style.overflow="hidden";
  tc.className="x-window-tc";
  tr.appendChild(tc);
  
  tcheader=document.createElement("div");
  tcheader.className="x-window-header x-unselectable ";
  tcheader.style.overflow="hidden";
  this.tool=document.createElement("div");
  //tool.className="x-tool-minimize";
  this.tool.className="x-tool x-tool-toggle x-tool-minimize";
  tcheader.appendChild(this.tool);
  
  tc.appendChild(tcheader);
  sp=document.createElement("span");
  sp.className="x-window-header-text";
  sp.style.cursor="move";
  sp.style.overflow="hidden";
  sp.style.textOverflow="ellipsis";//IE only
  sp.style.display="block";
  tcheader.appendChild(sp);
  
  this.titlebar.appendChild(tr);
  
  this.textNode=document.createTextNode(this.title);
  sp.appendChild(this.textNode);
  item.appendChild(this.titlebar);
  
  this.titlebar.style.height="20px";
  
  this.scrollarea = document.createElement("div");
  this.scrollarea.style.position="absolute";
  this.scrollarea.style.height = "15px";
  this.scrollarea.style.margin = "0px";
  this.scrollarea.style.padding= "0px";
  this.scrollarea.style.font="normal 10px verdana";
  
  this.scrollarea.style.whiteSpace="nowrap";
  this.scrollarea.style.textAlign="center";
  this.scrollarea.style.overflowX="hidden";
  this.scrollarea.style.overflowY="hidden";
  this.scrollarea.style.overflow="hidden";
  this.scrollarea.style.cursor="default";
  this.scrollarea.style.backgroundColor="white";
  
  
  
  
  this.wraparea = document.createElement("div");
  this.wraparea.className="x-window-bwrap";
  var ml = document.createElement("div");
  ml.className="x-window-ml";
  
  var mr = document.createElement("div");
  mr.className="x-window-mr";
 
  var mc = document.createElement("div");
  mc.className="x-window-mc";
  
  this.xbody = document.createElement("div");
  this.xbody.className="x-window-body";
  this.wraparea.appendChild(ml);
  ml.appendChild(mr);
  mr.appendChild(mc);
  mc.appendChild(this.xbody);
 
  this.xbody.appendChild(this.scrollarea);
  item.appendChild(this.wraparea);

  var bl = document.createElement("div");
  bl.className="x-window-bl";
  var br = document.createElement("div");
  br.className="x-window-br";
  var bc = document.createElement("div");
  bc.className="x-window-bc";
  var footer = document.createElement("div");
  footer.className="x-window-footer";
  bl.appendChild(br);
  br.appendChild(bc);
  bc.appendChild(footer);
  this.wraparea.appendChild(bl);
  
  return item;
}

/**
 * @param {int} w new width of the window. 
 * @param {int} h new height of the window. 
 **/
TableFigure.prototype.setDimension=function(/*:int*/ w,/*:int*/ h)
{
  draw2d.Figure.prototype.setDimension.call(this,w,h);
  if(this.titlebar!=null){this.titlebar.style.width=(this.getWidth()-6)+"px";}
   if(this.wraparea!=null)
  {
	   if(this.xbody!=null)
		   this.xbody.style.height=(this.getHeight()-20)+"px";
	if(this.scrollarea!=null){
		this.scrollarea.style.height=this.xbody.style.height;
		this.scrollarea.style.width=this.getWidth()-14+"px";
	}
    
  }
  for(key in this.children){
 	 var child = this.children[key];
 	 child.setDimension(this.getWidth(),child.getHeight());
  }
  if(this.portTop!=null){
		this.portTop.setPosition(this.getWidth()/2,0);
		this.portRight.setPosition(this.getWidth(),this.getHeight()/2+3);
		this.portBottom.setPosition(this.getWidth()/2,this.getHeight()+7);
		this.portLeft.setPosition(0,this.getHeight()/2+3);
	}
}

/**
 *
 **/
TableFigure.prototype.addChild=function(/*:draw2d.Button*/ item)
{
  this.children[item.id] = item;
  this.length= this.length+1;
  item.html.style.overflowX="hidden";
  item.html.style.overflowY="hidden";
  item.html.style.overflow="hidden";
  item.html.style.paddingLeft="0px";
  item.html.style.paddingRight="0px";
  item.html.style.left="1px";
  item.html.style.textAlign='left';
 
  item.classStyle="x-form-field";
  item.html.style.cursor="default"
  
  item.html.style.width="100%";
  item.html.height="20px";
  this.scrollarea.appendChild(item.getHTMLElement());
  //this.recalculateSize();
}


/**
 *
 **/
TableFigure.prototype.getChild=function(/*:String*/ id )
{
  return this.children[id];
}

TableFigure.prototype.expand = function(){
	this.tool.className="x-tool x-tool-toggle x-tool-minimize";
	this.resizeable=true;
	this.scrollarea.style.visibility="";
	this.setDimension(this.width,25+20*(this.length));
	this.open=true;
}
TableFigure.prototype.collapse = function(){
	this.tool.className="x-tool x-tool-toggle x-tool-maximize";
	this.scrollarea.style.visibility="hidden";
	this.resizeable=false;
	this.setDimension(this.width,25);
	this.open=false;
}
TableFigure.prototype.onDragstart = function(/*:int*/ x, /*:int*/ y)
{
	var originalResult = draw2d.WindowFigure.prototype.onDragstart.call(this,x,y);
	if( x<this.width && x>(this.width-20)&&y<20){
		if(this.open==true){
			this.open=false;
			this.collapse();
			
		}else{
			this.open=true;
			this.expand();
		}
		
		return false;
	}
	return originalResult;
};

TableFigure.prototype.addKeyColumn=function(name,type){
	var btn=new draw2d.Label('');
	btn.type=type;
	btn.columnName=name;
	btn.key=true;
	btn.setPosition(0,20*this.length+8);
	btn.setStyledText('<img src="icons/table_key.png" style="margin-left:4px;margin-right:5px;vertical-align:middle;border:0px" border="0" /><span class="x-form-field" style="font-size:11px;font-weight:bold">'+name+'</span>');
	this.addChild(btn);
};
TableFigure.prototype.addColumn=function(name,type,key){
	var btn=new draw2d.Label('');
	btn.columnName=name;
	btn.key=key;
	btn.setPosition(0,20*this.length+8);
	btn.type=type;
	var src=Ext.BLANK_IMAGE_URL;
	if(type!=null){
		if(type=="char")
			src="icons/font.png";
		else if(type=="number")
			src="icons/star.png";
		else if(type=="date")
			src="icons/date.png";
		else if(type=="time")
			src="icons/time.png";
		else if(type=="binary")
			src="icons/attach.png";
		else //if(type=="other")
			src="icons/database_table.png";
		
		
	}
	var txt;
	if(key){
		txt='<img width="16" height="16" src="icons/table_key.png" style="margin-left:4px;margin-right:0px;vertical-align:middle;border:0px" border="0" /><img src="'+src+'" width="16" height="16" style="margin-left:4px;margin-right:5px;vertical-align:middle;border:0px" border="0" /><span class="x-form-field" style="font-size:11px;font-weight:bold">'+name+'</span>'
	}else{
		txt='<img width="16" height="16" src='+Ext.BLANK_IMAGE_URL+' style="margin-left:4px;margin-right:0px;vertical-align:middle;border:0px"  border="0"  /><img src="'+src+'" width="16" height="16" style="margin-left:4px;margin-right:5px;vertical-align:middle;border:0px" border="0" /><span  style="font-size:11px" class="x-form-field">'+name+'</span>'
	}
	
	btn.setStyledText(txt);
	//btn.setStyledText('<img src="'+src+'" width="16" height="16" style="margin-left:4px;margin-right:5px;vertical-align:middle;border:0px" border="0" /><span  style="font-size:11px" class="x-form-field">'+name+'</span>');
	
	this.addChild(btn);
};

TableFigure.prototype.autoSize=function(){
	this.setDimension(200,25+20*(this.length));
}

TableFigure.prototype.addColumns=function(arr){
	ilength=arr.length;
	for(var i=0;i<ilength;i++){
		//if(arr[i].key=='1'){
		//	this.addKeyColumn(arr[i].cname,arr[i].type);
			
		//}
		//else{
			this.addColumn(arr[i].cname,arr[i].type,arr[i].key=='1'?true:false);
		//}
	}
	this.autoSize();
}
TableFigure.prototype.serialize=function(){
	var ser=new Object();
	ser["x"]=this.x;
	ser["y"]=this.y;
	ser["width"]=this.getWidth();
	ser["height"]=this.getHeight();
	ser["open"]=this.open;
	ser["title"]=this.title;
	ser["length"]=this.length;
	var arr=[];
	for(key in this.children){
	 	 var child = this.children[key];
	 	 var no=new Object();
	 	 no["columnName"]=child.columnName;
	 	 no["key"]=child.key;
	 	 no["type"]=child.type;
	 	 arr.push(no);
	}
	ser["children"]=arr;
	return ser;
}

TableCommandDelete=function(/*:draw2d.Figure*/ figure)
{
   //draw2d.Command.call(this,"delete figure");
   this.parent   = figure.parent; // CompartmentFigure
   this.figure   = figure;
   /** @private */
   this.workflow = figure.workflow;
   /** @private */
   this.connections = null;
   /** @private */
   this.compartmentDeleteCommands = null;
  // draw2d.CommandDelete.call(figure);
}

TableCommandDelete.prototype = new draw2d.Command;
/** @private **/
TableCommandDelete.prototype.type="TableCommandDelete";

TableCommandDelete.prototype.execute=function(){this.redo();};
TableCommandDelete.prototype.undo=function(){
	if(this.figure instanceof draw2d.CompartmentFigure){
		for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
			var _4f07=this.compartmentDeleteCommands.get(i);
			this.figure.addChild(_4f07.figure);
			this.workflow.getCommandStack().undo();
		}
	}
	this.workflow.addFigure(this.figure);
	if(this.figure instanceof draw2d.Connection){this.figure.reconnect();}
	this.workflow.setCurrentSelection(this.figure);
	if(this.parent!=null){
		this.parent.addChild(this.figure);
	}
	for(var i=0;i<this.connections.getSize();++i){
		this.workflow.addFigure(this.connections.get(i));
		this.connections.get(i).reconnect();
	}
};
TableCommandDelete.prototype.redo=function(){
	if(this.figure instanceof draw2d.CompartmentFigure){
		if(this.compartmentDeleteCommands==null){
			this.compartmentDeleteCommands=new draw2d.ArrayList();
			var _4f08=this.figure.getChildren().clone();
			for(var i=0;i<_4f08.getSize();i++){
				var child=_4f08.get(i);
				this.figure.removeChild(child);
				var _4f0b=new draw2d.CommandDelete(child);
				this.compartmentDeleteCommands.add(_4f0b);
				this.workflow.getCommandStack().execute(_4f0b);
			}
		}else{
			for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
				this.workflow.redo();
			}
		}
	}
	this.workflow.removeFigure(this.figure);
	this.workflow.setCurrentSelection(null);
	if(this.figure instanceof TableFigure &&this.connections==null){
		this.connections=new draw2d.ArrayList();
		var ports=this.figure.getPorts();
		for(var i=0;i<ports.getSize();i++){
			if(ports.get(i).getConnections){
				this.connections.addAll(ports.get(i).getConnections());
			}
		}
	}
	if(this.connections==null){
		this.connections=new draw2d.ArrayList();
	}
	if(this.parent!=null){this.parent.removeChild(this.figure);}
	for(var i=0;i<this.connections.getSize();++i){
		this.workflow.removeFigure(this.connections.get(i));
	}
};


TableFigure.prototype.createCommand=function( request)
{
	if(request.getPolicy() == draw2d.EditPolicy.DELETE){
		if(!this.isDeleteable())
			return null;
	    return new TableCommandDelete(this);
	}
	draw2d.Figure.prototype.createCommand.call(this,request);
}



TableMenu=function(){
	
	draw2d.Menu.call(this);
};

TableMenu.prototype=new draw2d.Menu();


TableMenu.prototype.createHTMLElement=function()
{
   var item = document.createElement("div");
//
   item.style.position="absolute";
   item.style.left   = this.x+"px";
   item.style.top    = this.y+"px";
   item.style.margin = "0px";
   item.style.padding= "0px";
   item.style.zIndex = ""+draw2d.Figure.ZOrderBaseIndex;
   item.style.border= "1px solid gray";
   //item.style.background = "lavender";
   item.style.cursor="pointer";
   item.className="x-menu";
   this.ul=document.createElement("ul");
   this.ul.className="x-menu-list";
   item.appendChild(this.ul);
  return item;
}

TableMenu.prototype.createList=function(){
	this.dirty=false;
	  //this.html.innerHTML="";
	  var oThis = this;
	  for(var i=0;i<this.menuItems.getSize();i++)
	  {
	      var item = this.menuItems.get(i);

	      var li = document.createElement("li");
	      if(item.getLabel()!=null){
	      //li.innerHTML = item.getLabel();
		      li.className="x-menu-list-item";
		      var a = document.createElement("a");
		      a.className="x-menu-item";
		      //a.innerHTML=item.getLabel();
		      li.appendChild(a);
		      var img=document.createElement("img");
		      img.className="x-menu-item-icon";
		      if(item.iconUrl!=null)
		    	  img.src=item.iconUrl;
		      else
		    	  img.src=Ext.BLANK_IMAGE_URL;
		      a.appendChild(img);
		      var textNode=document.createTextNode( item.getLabel());
		      a.appendChild(textNode);
		      this.ul.appendChild(li);
		     
		      li.menuItem = item;
		      a.menuItem = item;
		      if (li.addEventListener) 
		      {
		         li.addEventListener("click",  function(event)
		         {
		            var oEvent = arguments[0] || window.event;
		            oEvent.cancelBubble = true; 
		            oEvent.returnValue = false;
		            var diffX = oEvent.clientX;// - oThis.html.offsetLeft;
		            var diffY = oEvent.clientY;// - oThis.html.offsetTop;
		            var scrollLeft= document.body.parentNode.scrollLeft;
		            var scrollTop = document.body.parentNode.scrollTop;
		            this.menuItem.execute(diffX+scrollLeft, diffY+scrollTop);
		         }, false);
		         li.addEventListener("mouseup",  function(event){event.cancelBubble = true; event.returnValue = false;}, false);
		         li.addEventListener("mousedown",  function(event){event.cancelBubble = true; event.returnValue = false;}, false);
		         li.addEventListener("mouseover", function(event){ this.className="x-menu-list-item x-menu-item-active";},false);
		         li.addEventListener("mouseout", function(event){this.className="x-menu-list-item";},false);
		      } 
		      else if (li.attachEvent) 
		      {
		         li.attachEvent("onclick",  function(event)
		         {
		            var oEvent = arguments[0] || window.event;
		            var diffX = oEvent.clientX;// - oThis.html.offsetLeft;
		            var diffY = oEvent.clientY;// - oThis.html.offsetTop;
		            var scrollLeft= document.body.parentNode.scrollLeft;
		            var scrollTop = document.body.parentNode.scrollTop;
		            oEvent.cancelBubble = true; 
		            oEvent.returnValue = false;
		            oEvent.srcElement.menuItem.execute(diffX+scrollLeft, diffY+scrollTop);
		         });
		         li.attachEvent("onmousedown",  function(event){event.cancelBubble = true; event.returnValue = false;});
		         li.attachEvent("onmouseup",  function(event){event.cancelBubble = true; event.returnValue = false;});
		         //li.attachEvent("onmouseover", function(event){event.srcElement.style.backgroundColor="silver";});
		         //li.attachEvent("onmouseout", function(event){event.srcElement.style.backgroundColor="transparent";});
		         li.attachEvent("onmouseover", function(event){event.srcElement.style.className="x-menu-list-item x-menu-item-active";});
		         li.attachEvent("onmouseout", function(event){event.srcElement.style.className="x-menu-list-item";});
		      }
	      }else{
	    	  li.className="x-menu-list-item x-menu-sep-li";
	    	  var sp = document.createElement("span");
	    	  sp.className="x-menu-sep";
	    	  sp.innerHTML = "&#160;";
	    	  li.appendChild(sp);
	    	  this.ul.appendChild(li);
	      }
	  }

};

TableFigure.prototype.getContextMenu=function()
{
  var menu =new TableMenu();
  var oThis = this;

  menu.appendMenuItem(new draw2d.MenuItem("To Front", "icons/shape_move_forwards.png",function(){
	  oThis.workflow.moveFront(oThis);
	  	})
  );
  menu.appendMenuItem(new draw2d.MenuItem("To Back", "icons/shape_move_backwards.png",function(){
	  oThis.workflow.moveBack(oThis);
  		})
  );
  
  menu.appendMenuItem(new draw2d.MenuItem(null,null,null));
  menu.appendMenuItem(new draw2d.MenuItem("Delete","icons/shape_square_delete.png",function(){
	  oThis.onKeyDown(46,false);
  	})
  );
  //menu.appendMenuItem(new draw2d.MenuItem("Silver", null,function(){oThis.setBackgroundColor(new  draw2d.Color(128,128,128));}));
  //menu.appendMenuItem(new draw2d.MenuItem("Black", null,function(){oThis.setBackgroundColor(new  draw2d.Color(0,0,0));}));
  return menu;
}

/*
 * TableWorkflow=function(element){
	draw2d.Workflow.call(this,element);
	this.addremoveListeners=new draw2d.ArrayList();
}
TableWorkflow.prototype = new draw2d.Workflow;
 */

TableConnection.prototype.serialize=function(){
	var ser=new Object();
	ser["source"]=this.sourcePort.parentNode.title;
	ser["target"]=this.targetPort.parentNode.title;
	return ser;
}
function FormGridFactory(){
};
function formgridbutton(item){
	  if(item.id=='next'){
		  if(item.formGrid.rowIndex<item.formGrid.myData.length-1){
			  item.formGrid.rowIndex=item.formGrid.rowIndex+1;
		  }
	  }
	  else if(item.id=='prev'){
		  if(item.formGrid.rowIndex>0){
			  item.formGrid.rowIndex=item.formGrid.rowIndex-1
		  }
	  }
	  else if(item.id=='first'){
		  item.formGrid.rowIndex=0
	  }
	  else {
		  item.formGrid.rowIndex=item.formGrid.myData.length-1;
	  }
	  
	  var myData2 = [];
  	if(item.formGrid.myData.length>0){
	    	for(i=0;i<item.formGrid.meta.length;i++){
	    		var rc=[];
	    		rc.push(item.formGrid.meta[i]["l"]);
	    		rc.push(item.formGrid.myData[item.formGrid.rowIndex][i]);
	    		myData2.push(rc);
	    	}
  	}
  	item.formGrid.store.loadData(myData2);
}; //end formgridbutton

FormGridFactory.prototype.build=function(queryID,meta,myData){
	 var store2 = new Ext.data.SimpleStore({
         fields: [
            {name: 'column',sortable: false},
            {name: 'value',sortable: false}
         ]
     });

     var formTB=new Ext.Toolbar({items:[]});
     var rowIndex=0;
     var firstRender=true;
     var formGrid = new Ext.grid.GridPanel({
         store: store2,
         formTB:formTB,
         columns: [
             {header: "Column",  dataIndex: 'column',
              	renderer:defaultRenderer},
             {header: "Value", dataIndex: 'value',width:250,renderer:defaultRenderer}
         ],
         stripeRows: true,
         myData:myData,
         meta:meta,
         queryID:queryID,
         rowIndex:rowIndex,
         additionalData:function(addedData,totalData){},
         refreshData:function(newData){var myData2 = [];
         	this.rowIndex=0;
	    	if(this.myData.length>0){
		    	for(i=0;i<this.meta.length;i++){
		    		var rc=[];
		    		rc.push(this.meta[i]["l"]);
		    		rc.push(this.myData[this.rowIndex][i]);
		    		myData2.push(rc);
		    	}
	    	}
		    this.store.loadData(myData2);
		  },
         
	     listeners: {
	     	render: function(){
			  if(firstRender){
   	       		gridnext=new Ext.Toolbar.Button({
   	       			cls: 'x-btn-icon',
   	       			icon:'icons/resultset_next.png',
   	  				tooltip:'<b>Next Record</b>',
   	  				handler:formgridbutton,
   	  				id:'next',
   	  				formGrid:this
   	  		    });
   	    	          
	    	    gridprev=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_previous.png',
	    			tooltip:'<b>Previous Record</b>',
   	  				handler:formgridbutton	,
   	  				id:'prev',
   	  				formGrid:this
	    	    });
	    	         
	    	    gridfirst=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_first.png',
	  				tooltip:'<b>First Record</b>'	,
	  				handler:formgridbutton,
	  				id:'first',
	  				formGrid:this
	  		    });
	    	          
	    	    gridlast=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_last.png',
	    	    	tooltip:'<b>Last Available Record</b>',
   	  				handler:formgridbutton	,
   	  				id:'last',
   	  				formGrid:this
	    		});
	    	    this.formTB.add(gridfirst);
	    	    this.formTB.add(gridprev);
	    	    this.formTB.add(gridnext);
	    	    this.formTB.add(gridlast);
	    	    firstRender=false;
			  }
	    	    var myData2 = [];
	    	    this.rowIndex=0;
		    	if(this.myData.length>0){
			    	for(i=0;i<this.meta.length;i++){
			    		var rc=[];
			    		rc.push(this.meta[i]["l"]);
			    		rc.push(this.myData[this.rowIndex][i]);
			    		myData2.push(rc);
			    	}
		    	}
			    this.store.loadData(myData2);
     		}
     	}
     	,bbar:formTB
     });
     return formGrid;
}

FormGridFactory.prototype.name=function(){
	return "Form";
}

sqlResultPluginFactories.push(new FormGridFactory());

var sqlTableTemplate=new Ext.XTemplate('<table class="sqltable">',
		 '<thead><tr><tpl for="meta">',
		 '<th class="sqlth">{[this.encode(values["l"])]}</th>',
		 '</tpl></tr></thead>','<tpl for="data"><tr><tpl for=".">',
		 '<td class="sqltd" align="">{[this.encode(values)]}</td>',
		 '</tpl></tr></tpl></table>',{encode:function(val){return Ext.util.Format.htmlEncode(val);}}
);
sqlTableTemplate.compile();

	function HTMLGridFactory(){
	};
	HTMLGridFactory.prototype.build=function(queryID,meta,myData){
		return new Ext.Panel({
			html:'',autoScroll:true,
			myData:myData,
			queryID:queryID,
			meta:meta,
			loaded:false,
			listeners:{
	    		render: function(){
	        	 	this.loaded=true;
	        		var tmpObject=new Object();
	        		tmpObject["data"]=this.myData;
	        		tmpObject["meta"]=this.meta;
	        		sqlTableTemplate.overwrite(this.body, tmpObject, true);
	        	
	    		}
	    	},
			additionalData:function(addedData,totalData){
	    		if(this.loaded){
		    		var tmpObject=new Object();
		    		tmpObject["data"]=this.myData;
		    		tmpObject["meta"]=this.meta;
		    		sqlTableTemplate.overwrite(this.body, tmpObject, true);
	    		}
	    	},
	    	refreshData:function(newData){
	    		if(this.loaded){
		    		var tmpObject=new Object();
		    		tmpObject["data"]=this.myData;
		    		tmpObject["meta"]=this.meta;
		    		sqlTableTemplate.overwrite(this.body, tmpObject, true);
	    		}
	    	}
		});
	}
	HTMLGridFactory.prototype.name=function(){
		return "HTML";
	}

sqlResultPluginFactories.push(new HTMLGridFactory());
Ext.Ajax.timeout = 60000;
var dynTabsArray = new Object();
var dynMenuArray = new Object();
var loaded_plugin_scripts=new Object();
var southPanel;
var printingWindow;
Array.prototype.clear=function()
{
    this.length = 0;
};

var sqlPrintingTableTemplate=new Ext.XTemplate('<table class="sqltable">',
		 '<thead><tr><tpl for="meta">',
		 '<th class="sqlth">{[this.encode(values["l"])]}</th>',
		 '</tpl></tr></thead>','<tpl for="data"><tr><tpl for=".">',
		 '<td class="sqltd">{[this.encode(values)]}</td>',
		 '</tpl></tr></tpl></table>',{
		encode:function(val){
			return Ext.util.Format.htmlEncode(val);
		}
	}
	);
//sqlPrintingTableTemplate.compile();

TreeStoreLoader = function(config) {

    /**
     * name of the parameter in the HTTP request (QueryString or POST data)
     */
    this.nodeParamName = "node";

    // required to be set to "true" (or non empty string) by the parent class to work properly
    this.dataUrl = true;

    Ext.apply(this, config);

    if (!this.dataFields) {
        this.dataFields = ['id', 'text', 'children','leaf','type','cls','qname'];
    }
    for (var i = 0, l = this.dataFields.length; i < l; i++) {
        if (typeof this.dataFields[i] == "string") {
            this.dataFields[i] = {
                name : this.dataFields[i]
            };
        }
    }

    if (!this.store) {
        this.store = new Ext.data.JsonStore({
            url : this.dataUrl,
            root : this.dataRoot,
            fields : ['text']
        });
    }

    TreeStoreLoader.superclass.constructor.call(this);
};

Ext.extend(TreeStoreLoader, Ext.tree.TreeLoader, {

    requestData : function(node, callback) {
        if (this.fireEvent("beforeload", this, node, callback) !== false) {
            this.store.purgeListeners();
            this.store.on('load', this.handleResponse.createDelegate(this, [node, callback], 0));
            this.store.on('loadexception', this.handleFailure.createDelegate(this, [node, callback], 0));
            this.store.load({
                params : this.getParams(node),
                callback : callback
            });
        } else {
            if (typeof callback == "function") {
                callback();
            }
        }
    },

    getParams : function(node) {
        var params = {};
        var bp = this.baseParams;
        for (var key in bp) {
            if (typeof bp[key] != "function") {
                params[key] = bp[key];
            }
        }
        params[this.nodeParamName] = node.id;
        return params;
    },

    processResponse : function(node, callback) {
        try {
            this.addChildren(node);
            if (typeof callback == "function")
                callback();
        } catch (e) {
            this.handleFailure(node, callback);
        }
    },

    handleResponse : function(node, callback) {
        this.transId = false;
        this.processResponse(node, callback);
        this.fireEvent("load", this, node);
    },

    handleFailure : function(node, callback) {
        this.transId = false;
        this.fireEvent("loadexception", this, node);
        if (typeof callback == "function") {
            callback(this, node);
        }
    },

    addChildren : function(parent) {
        this.store.each(function(rec) {
            parent.appendChild(this.createChild(rec));
        }, this);
        if (parent.attributes.expanded === true) {
            parent.expand();
        }
    },

    createChild : function(rec) {
        var attr = {};
        for (var i = 0, f = this.dataFields; i < f.length; i++) {
            attr[f[i].name] = Ext.isEmpty(f[i].mapping) ? rec.get(f[i].name) : ((typeof f[i].mapping == 'function')
                    ? f[i].mapping(rec)
                    : rec.get(f[i].mapping));
        }
        return this.createNode(attr);
    }

});

var httpDatabasesProxy = new Ext.data.HttpProxy( {
	url :'do?action=getDatabases',
	method :"POST"
});

var databasesReader = new Ext.data.JsonReader( {
	root :'result.databases',
	id :'id'
}, [ 'id', 'iconurl','name','hasCatalogs','catalogs','catalog','autocommit' ]);

var databasesDataStore = new Ext.data.Store( {
	proxy :httpDatabasesProxy,
	reader :databasesReader
});
databasesDataStore.load();

var httpSourcesProxy = new Ext.data.HttpProxy( {
	url :'do?action=getSources',
	method :"POST"
});


var httpTreeProxy = new Ext.data.HttpProxy( {
	url :'do?action=getTree',
	method :"POST"
});

var treeReader = new Ext.data.JsonReader( {
	root :'result.nodes',
	id :'id'
}, [ 'id', 'text', 'leaf', 'type', 'cls', 'qname' ]);


var sourcesReader = new Ext.data.JsonReader( {
	root :'result.sources',
	id :'id'
}, [ 'id', 'iconurl', 'sourceName', 'jdbcUrl', 'driverid', 'userName' ]);



var sourcesDataStore = new Ext.data.Store( {
	proxy :httpSourcesProxy,
	reader :sourcesReader
});


var httpDriverProxy = new Ext.data.HttpProxy( {
	url :'do?action=getDrivers',
	method :"POST"
});

var driverReader = new Ext.data.JsonReader( {
	root :'result.drivers',
	id :'id'
}, [ 'id', 'drvname', 'drvclassname', 'exampleurl', 'valid', 'icon' ]);

var driversDataStore = new Ext.data.Store( {
	proxy :httpDriverProxy,
	reader :driverReader
});



var _counter = 0;
var preferredLayout = 0;
var queryTextPanel;
function nextCounter() {
	return _counter++;
}
function defaultRenderer(val) {
	return Ext.util.Format.htmlEncode(val);
}
var homePage = new Ext.Panel({
	autoScroll :true,
	border:false,
	title :"<img src='icons/house.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;&nbsp;Home"
});



var tabpanelpages = new Ext.TabPanel( {
	region :'center',
	border :false,
	id :'tabpanelpages',
	tabPosition :'top',
	activeTab :0,
	deferredRender :false,
	items : [ homePage ]
});

var xSourceTempl = new Ext.XTemplate(
		'<tpl for=".">',
		'<div class="x-combo-list-item" style="cursor:default;" id="{id}"><img src="{iconurl}" onerror="this.src=\'icons/database.png\'" style="vertical-align:middle;width:16px;height:16px;margin-right:5px" />{sourceName}</div>',
		'</tpl>', '<div class="x-clear"></div>');

var xdriverTempl = new Ext.XTemplate(
		'<tpl for=".">',
		'<div class="x-combo-list-item" style="cursor:default;" id="{id}">',
		'<img src="{icon}" onerror="this.src=\'icons/database.png\'" style="vertical-align:middle;width:16px;height:16px" />',
		' <tpl if="valid">',
		'<span style=\'font-weight: bolder\'>{drvname}</span>', '</tpl>',
		' <tpl if="!valid">',
		'<span><s>{drvname}</s></span>',
		'</tpl>', '</div>', '</tpl>', '<div class="x-clear"></div>');

var srcDrvTpl=new Ext.XTemplate('<tpl for="."><div class="x-combo-list-item"><img src="{icon}" onerror="this.src=\'icons/database.png\'" style="vertical-align:middle;width:16px;height:16px;margin-right:5px" /><tpl if="valid"><span style=\'font-weight: bolder\'>{drvname}</span></tpl><tpl if="!valid"><s>{drvname}</s></tpl></div></tpl>');

var sourcesview;
var driversview;

var outlineTempl = new Ext.XTemplate('<tpl for=".">',
		'<div class="x-combo-list-item" style="cursor:default;" >{title}</div>',
		'</tpl>', '<div class="x-clear"></div>');
var xConnTempl = new Ext.XTemplate(
		'<tpl for=".">',
		'<div class="x-combo-list-item" style="cursor:default;" id="{id}"><img src="{iconurl}" onerror="this.src=\'icons/database.png\'" style="vertical-align:middle;width:16px;height:16px;margin-right:5px" />{name}</div>',
		'</tpl>', '<div class="x-clear"></div>');

var menu_newconnection;
function commitConnection(titem) {
	var selectedConnection = databasesDataStore.getAt(connection_menu.rowid);
	var sessionid = selectedConnection.get('id');
	commitConnectionImpl(sessionid);
} // end commitConnection

function commitConnectionImpl(sessionid) {
	new Ext.data.Connection().request( {
		url :'do?action=commitConnection&sessionid=' + sessionid,
		method :'post',
		scope :this,
		params :this.baseParams,
		failure :requestFailed

	});
}// end commitConnectionImpl

function rollbackConnection(titem) {

	var selectedConnection = databasesDataStore.getAt(connection_menu.rowid);
	var sessionid = selectedConnection.get('id');
	rollbackConnectionImpl(sessionid);
}// end rollbackConnection

function rollbackConnectionImpl(sessionid) {
	new Ext.data.Connection().request( {
		url :'do?action=rollbackConnection&sessionid=' + sessionid,
		method :'post',
		scope :this,
		params :this.baseParams,
		failure :requestFailed
	});
}// end rollbackConnectionImpl
function closeConnection(titem) {

	var selectedConnection = databasesDataStore.getAt(connection_menu.rowid);
	var sessionid = selectedConnection.get('id');
	new Ext.data.Connection().request( {
		url :'do?action=closeConnection&sessionid=' + sessionid,
		method :'post',
		scope :this,
		params :this.baseParams,
		callback : function(options, bSuccess, response) {
			databasesDataStore.reload();
		}
	});
}// end of deleteConnection

function createPage() {

	Ext.QuickTips.init();
	httpSourcesProxy.getConnection().on('requestexception', requestFailed);
	
	var sourcemenu = createSourceMenu();
	var drivermenu = createDriverMenu();

	sourcesview = new Ext.DataView( {
		tpl :xSourceTempl,
		emptyText :'<div style="overflow: hidden;padding:5px"><b>No sources yet</b><div style="overflow: hidden;margin-left:5px;margin-top:5px">You have to define at least one source in order to connect to your database.<br/><br/><a href="#" onclick="newsourcesPanel()"><img style="border:0;text-decoration: none;width:16px;height:16px;vertical-align:middle;margin-right:5px" src="icons/transmit_add.png"/>Create a new source</a></div></div><div class="x-clear"></div>',
		singleSelect :true,
		selectedClass :"ydataview-selected",
		store :sourcesDataStore,
		itemSelector :'div.x-combo-list-item',
		deferEmptyText: false
	});
	
	driversview = new Ext.DataView( {
		tpl :xdriverTempl,
		emptyText :'No Driver yet',
		singleSelect :true,
		selectedClass :"ydataview-selected",
		store :driversDataStore,
		itemSelector :'div.x-combo-list-item',
		deferEmptyText: false
	});

	sourcesview.on('contextmenu', function(vw, idx, nd, e) {
		e.stopEvent();
		sourcesview.select(nd);
		sourcemenu.rowid = idx;
		var coords = e.getXY();
		sourcemenu.showAt( [ coords[0] - 50, coords[1] ]);
	});
	
	driversview.on('contextmenu', function(vw, idx, nd, e) {
		e.stopEvent();
		driversview.select(nd);
		drivermenu.rowid = idx;
		var coords = e.getXY();
		drivermenu.showAt( [ coords[0] - 50, coords[1] ]);
	});

	sourcesview.on('dblclick', function(vw, idx, nd, e) {
		e.stopEvent();
		openConnectionDialog(sourcesDataStore.getAt(idx));
	});
	driversview.on('dblclick', function(vw, idx, nd, e) {
		e.stopEvent();
		//openConnectionDialog(sourcesDataStore.getAt(idx));
	});

	menu_newconnection = new Ext.menu.Menu( {
		items : []
	});

	menu_newconnection.on('beforeshow', function() {
		menu_newconnection.removeAll();
		for ( var i = 0; i < sourcesDataStore.getCount(); i++) {
			menu_newconnection.addMenuItem(new Ext.menu.Item( {
				text :sourcesDataStore.getAt(i).get('sourceName'),
				idx :i,
				icon :'icons/database.png',
				handler : function() {
					openConnectionDialog(sourcesDataStore.getAt(this.idx));
				}
			}));
		}
		menu_newconnection.addSeparator();
		menu_newconnection.addMenuItem(new Ext.menu.Item( {
			text :'New Source...',
			icon :'icons/user_add.png',
			handler :newsourcesPanel
		}));
		menu_newconnection.addSeparator();
		menu_newconnection.addMenuItem(new Ext.menu.Item( {
			text :'New SQL Editor',
			icon :'icons/page_edit.png',
			handler : function() {
				newEditor("");
			}
		}));

	});
	
	connection_menu = new Ext.menu.Menu( {
		//shadow :'frame',
		//id :'connectionMenu'
			items : []
	});


	connectionsview = new Ext.DataView( {
		emptyText :'<div style="margin:5px"><span><b>No connections yet</b></span><br/><br/>You can connect to any defined source</div><div class="x-clear"></div>',
		//emptyText :'You can connect to any defined source',
		tpl :xConnTempl,
		singleSelect :true,
		selectedClass :"ydataview-selected",
		store :databasesDataStore,
		itemSelector :'div.x-combo-list-item',
		deferEmptyText: false
	});

	connectionsview.on('contextmenu', function(vw, idx, nd, e) {

		e.stopEvent();
		connectionsview.select(nd);
		connection_menu.rowid = idx;
		var coords = e.getXY();
		
		connection_menu.removeAll();
		var selectedConnection = databasesDataStore
				.getAt(connection_menu.rowid);
		connection_menu.addMenuItem(new Ext.menu.Item( {
			text :'New SQL Editor',
			icon :'icons/page_edit.png',
			handler : function() {
				var editor = newEditor("");
				editor.tb.cmb.setValue(selectedConnection.get('id'));
				editor.tb.catcmb.setValue(selectedConnection.get('catalog'));
			}
		}));
		connection_menu.addSeparator();
		connection_menu.addMenuItem(new Ext.menu.Item( {
			text :'Close Connection',
			icon :'icons/disconnect.png',
			handler :closeConnection
		}));

		if (selectedConnection.get('autocommit') == false) {
			connection_menu.addSeparator();
			connection_menu.addMenuItem(new Ext.menu.Item( {
				text :'Commit',
				icon :'icons/database_save.png',
				handler :commitConnection
			}));
			connection_menu.addMenuItem(new Ext.menu.Item( {
				text :'Rollback',
				icon :'icons/arrow_undo.png',
				handler :rollbackConnection
			}));
		}
		
		connection_menu.showAt( [ coords[0] - 50, coords[1] ]);
	});

	sourcesDataStore.on('load', loadSuccessful);
	sourcesDataStore.on('loadexception', loadFailed);
	
	driversDataStore.on('load', loadSuccessful);
	driversDataStore.on('loadexception', loadFailed);


	function loadSuccessful(store, recordArray, options) {
		//alert(sourcesDataStore.getCount());
		//alert('success');
		// alert(recordArray);
	}

	function loadFailed(proxy, options, response, error) {
		var object = Ext.util.JSON.decode(response.responseText);

		Ext.MessageBox.show( {
			title :'Error Message',
			msg :object.error,
			buttons :Ext.MessageBox.OK,
			icon :Ext.MessageBox.ERROR
		});

	}
	
	viewport = createViewport();
	viewport.doLayout();
	homePage.load({nocache:true, url: "home.jsp", scripts:true});
	sourcesDataStore.load();
	
	driversDataStore.load();
	
	document.getElementById('loading-msg').innerHTML = 'Done.';
	setTimeout( function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut( {
			remove :true,
			duration :1.5
		});
//		 if(window.console && window.console.firebug){
//			 Ext.MessageBox.show({
//			 title: 'Warning',
//			 msg: 'Firebug is known to cause performance issues with Ext JS.',
//			 buttons: Ext.MessageBox.OK,
//			 icon: Ext.MessageBox.WARNING
//			 });
//		 }
	}, 250);

	Ext.TaskMgr.start({
	    run: function(){
			new Ext.data.Connection().request( {
				url :'do?action=ping',
				method :'post',
				scope :this
			});
		
		},
	    interval: 120000
	});
	
}

function createViewport() {
	var sourcesPanel = new Ext.Panel(
	{
		items :sourcesview,
		id :'eastaliases',
		title :"<img src='icons/transmit.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Sources",
		autoScroll :true,
		containerScroll :false,
		layout:'fit',
		border :false,
		tbar : [ {
			tooltip :'<b>New Source</b><br/>Create a new source',
			icon :'icons/transmit_add.png',
			cls :'x-btn-icon',
			handler :newsourcesPanel
		} ]
	});
	
	var driversPanel = new Ext.Panel(
	{
		items :driversview,
		id :'eastdrivers',
		title :"<img src='icons/application.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Drivers",
		layout:'fit',
		autoScroll :true,
		
		border :false,
		tbar : [ {
			tooltip :'<b>New driver</b><br/>Create a new driver',
			icon :'icons/application_add.png',
			cls :'x-btn-icon',
			handler :newdriversPanel
		} ]
	});
	
	var connectionsPanel = new Ext.Panel( {
		items :connectionsview,
		id :'eastconnections',
		title :"<img src='icons/database.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Connections",
		border :false,
		autoScroll :true,
		tbar : [ {
			cls :'x-btn-text-icon',
			text :'New Connection...',
			menu :menu_newconnection,
			style :'width:20px',
			icon :"icons/database_lightning.png"
		} ]
	});
	
	southPanel = new Ext.TabPanel( {
		region :'south',
		border :false,
		id :'southPanel',
		minTabWidth :115,
		border :false,
		tabWidth :135,
		enableTabScroll :false,
		border :false,
		height :250,
		split :true
	});
	
	var tree=createTree();
	

	 var report = new Ext.Toolbar.Button( {
			cls :'x-btn-icon',
			icon :'icons/report.png',
			tooltip :'<b>Export [Not yet implemented]</b><br/>Export details....',
			handler : function() {
			}
	});
	
	
	var viewport= new Ext.Viewport( {
		renderTo :Ext.getBody(),
		layout :"border",
		items : [ {
			region :"center",
			layout :"border",
			border :false,
			items : [ tabpanelpages ]
		}, {
			region :"west",
			title:"<img src='icons/database.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Database View",
			width :280,
			split :true,
			collapsible :true,
			titleCollapse :true,
			border :false,
			layout :"fit",
			items : tree/* {
				//xtype :"tabpanel",
				activeTab :0,
				deferredRender :false,
				items : [tree]
			}*/
		}, {
			region :"south",
			title :"<img src='icons/application_view_detail.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Details",
			height :200,
			split :true,
			collapsible :true,
			titleCollapse :true,
			items :southPanel,
			border :false,
			layout :'fit'
                        //,
			//tbar : [report ]
				
		}, {
			region :"east",
			title :"<img src='icons/application_cascade.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Sources, Drivers, Active Connections",
			width :300,
			split :true,
			collapsible :true,
			titleCollapse :true,
			layout :'fit',
			//autoScroll :true,
			border :false,
			items : [ {
				xtype :"tabpanel",
				activeTab :0,
				deferredRender :false,
				items : [ sourcesPanel,driversPanel,connectionsPanel ]
			} ]
		} ]
	});
	
	return viewport;
}// end createViewport



function editsourcesPanel(source) {

	var config = {
		width :600,
		height :300,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Edit Source ' + source.get('sourceName')
	};

	var dialog = new Ext.Window(config);
	dialog.addButton('Close', dialog.close, dialog);

	var sourceForm = new Ext.form.FormPanel( {
		labelWidth :95,
		onSubmit :Ext.emptyFn,
		url :'do?action=updateSource',
		baseCls :'x-plain'
	});

	function submitSuccessful(form, action) {
		dialog.close();
		sourcesDataStore.reload();

	}

	jdbcurl = new Ext.form.TextField( {
		fieldLabel :'JDBC URL',
		name :'url',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :source.get('jdbcUrl')
	});
	updateSourceDriverCombo = new Ext.form.ComboBox( {
		fieldLabel :'Driver Name',
		store :driversDataStore,
		displayField :'drvname',
		valueField :'id',
		mode :'local',

		editable :false,
		triggerAction :'all',
		selectOnFocus :true,
		width :400,
		forceSelection :true,
		tpl: srcDrvTpl
	});
	
	updateSourceDriverCombo.on('select', function(cmb, rc, idx) {
		jdbcurl.setValue(rc.get('exampleurl'));
	});
	updateSourceDriverCombo.setValue(source.get('driverid'));
	
	sourceForm.add(new Ext.form.TextField( {
		fieldLabel :'Source Name',
		name :'sourceName',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :source.get('sourceName')
	}), //updateAliasDriverCombo, 
	jdbcurl,updateSourceDriverCombo,new Ext.form.TextField( {
		fieldLabel :'User Name',
		name :'user',
		width :400,
		readOnly :false,
		allowBlank :true,
		value :source.get('userName')
	})
	);

	function onSourceUpdateSubmit() {
		if (sourceForm.form.isValid()) {
			sourceForm.form.submit( {
				params : {
					//driverid :updateAliasDriverCombo.getValue(),
				driverid :updateSourceDriverCombo.getValue(),
				sourceid :source.get('id')
				},
				waitMsg :'Updating Source...',
				failure :submitFailed,
				success :submitSuccessful
			});
		} else {
			Ext.MessageBox.show( {
				title :'Error',
				msg :'Please fix the errors noted.',
				buttons :Ext.MessageBox.OK,
				icon :Ext.MessageBox.ERROR
			});

		}

	}
	dialog.addButton('Update', onSourceUpdateSubmit, dialog);
	dialog.show();
	sourceForm.render(dialog.body);
}// End of editsourcesPanel


function editdriversPanel(driver) {

	var config = {
		width :600,
		height :300,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Edit Driver ' + driver.get('drvname')
	};

	var dialog = new Ext.Window(config);
	dialog.addButton('Close', dialog.close, dialog);

	var driverForm = new Ext.form.FormPanel( {
		labelWidth :95,
		url :'do?action=updateDriver',
		baseCls :'x-plain'
	});
	driverForm.on('actioncomplete', function(form, action) {
		if (action.result.success === true) {
			driversDataStore.reload();
			dialog.close();
		}
	});
	driverForm.on('actionfailed', function(form, action) {
		Ext.MessageBox.show( {
			title :'Error',
			msg :action.result.error,
			buttons :Ext.MessageBox.OK,
			icon :Ext.MessageBox.ERROR
		});
	});

	driverForm.add(new Ext.form.TextField( {
		fieldLabel :'Driver Name',
		name :'drivername',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :driver.get('drvname')
	}),

	new Ext.form.TextField( {
		fieldLabel :'Driver Class',
		name :'driverclass',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :driver.get('drvclassname')
	}), new Ext.form.TextField( {
		fieldLabel :'Example URL',
		name :'exampleurl',
		width :400,
		readOnly :false,
		allowBlank :true,
		value :driver.get('exampleurl')
	})

	);

	function onDriverUpdateSubmit() {
		driverForm.form.submit( {
			params : {
				driverid :driver.get('id')
			},
			waitMsg :'Updating Driver...'
		});
	}
	dialog.addButton('Update', onDriverUpdateSubmit, dialog);

	dialog.show();
	driverForm.render(dialog.body);

}// End of editsourcesPanel


function requestFailed(connection, response, options) {

	Ext.MessageBox.show( {
		title :'Error',
		msg :"Status: " + response.status + ", Status Text: "
				+ response.statusText,
		buttons :Ext.MessageBox.OK,
		icon :Ext.MessageBox.ERROR
	});

}// end requestFailed


function newsourcesPanel() {
	// e.stopEvent();
	var config = {

		width :500,
		height :250,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'New Source'
	};

	var dialog = new Ext.Window(config);

	dialog.addButton('Close', dialog.close, dialog);

	var sourceForm = new Ext.form.FormPanel( {
		labelWidth :95,
		url :'do?action=createSource',
		onSubmit :Ext.emptyFn,
		baseCls :'x-plain'
	});

	
	var newSourceDriverCombo = new Ext.form.ComboBox( {
		fieldLabel :'Driver Name',
		store :driversDataStore,
		displayField :'drvname',
		valueField :'id',
		mode :'local',
		editable :false,
		triggerAction :'all',
		selectOnFocus :true,
		width :300,
		forceSelection :true,
		tpl: srcDrvTpl
	});
	
	
	var jdbcurl = new Ext.form.TextField( {
		fieldLabel :'JDBC URL',
		name :'url',
		width :300,
		readOnly :false,
		allowBlank :false
	});
	
	sourceForm.add(new Ext.form.TextField( {
		fieldLabel :'Source Name',
		name :'sourceName',
		width :300,
		readOnly :false,
		allowBlank :false
	}),  jdbcurl, newSourceDriverCombo, 
	new Ext.form.TextField( {
		fieldLabel :'User Name',
		name :'user',
		width :300,
		readOnly :false,
		allowBlank :true
	})
	
	);

	function onSourceSubmit() {
		if (sourceForm.form.isValid()) {
			sourceForm.form.submit( {
				params : {
					driverid :newSourceDriverCombo.getValue()
				},
				waitMsg :'Creating Source...',
				failure :submitFailed,
				success :submitSuccessful
			});
		} else {

			Ext.MessageBox.show( {
				title :'Error',
				msg :'Please fix the errors noted.',
				buttons :Ext.MessageBox.OK,
				icon :Ext.MessageBox.ERROR
			});
		}
	}

	function submitSuccessful(form, action) {
		sourcesDataStore.reload();
		dialog.close();
	}
	dialog.addButton('Create', onSourceSubmit, dialog);
	dialog.show();
	sourceForm.render(dialog.body);

}// End of newsourcesPanel


function newdriversPanel() {
	// e.stopEvent();
	var config = {

		width :500,
		height :200,
		shadow :true,
		minWidth :300,
		minHeight :150,
		modal :true,
		collapsible :false,
		closable :true,
		title :'New driver'
	};

	var dialog = new Ext.Window(config);

	dialog.addButton('Close', dialog.close, dialog);

	var driverForm = new Ext.form.FormPanel( {
		labelWidth :95,
		url :'do?action=createDriver',
		baseCls :'x-plain',
		onSubmit :Ext.emptyFn
	});

	driverForm.add(new Ext.form.TextField( {
		fieldLabel :'Driver Name',
		name :'drivername',
		width :300,
		readOnly :false,
		allowBlank :false,
		value :''
	}),

	new Ext.form.TextField( {
		fieldLabel :'Driver Class',
		name :'driverclass',
		width :300,
		readOnly :false,
		allowBlank :false,
		value :''
	}), new Ext.form.TextField( {
		fieldLabel :'Example URL',
		name :'exampleurl',
		width :300,
		readOnly :false,
		allowBlank :true,
		value :''
	})

	);

	function onDriverNewSubmit() {
		if (driverForm.form.isValid()) {
			driverForm.form.submit( {
				params : {

				},
				waitMsg :'Creating New Driver...',
				failure :submitFailed,
				success :submitSuccessful
			});
		}
	}
	function submitSuccessful(form, action) {
		driversDataStore.reload();
		dialog.close();
	}
	dialog.addButton('Create', onDriverNewSubmit, dialog);

	dialog.show();
	driverForm.render(dialog.body);

}// End of newdriversPanel

function submitFailed(form, action) {

	var failureMessage = "Error occurred.";
	if (action.failureType == Ext.form.Action.LOAD_FAILURE) {
		failureMessage = action.result.message;
	} else if (action.failureType == Ext.form.Action.CONNECT_FAILURE) {

		failureMessage = "Status: " + action.response.status
				+ ", Status Text: " + action.response.statusText;
	}

	else if (action.failureType == Ext.form.Action.SERVER_INVALID) {

		failureMessage = action.result.message;
	}

	else if (action.failureType == Ext.form.Action.CLIENT_INVALID) {
		failureMessage = "Please fix any and all validation errors.";
	}

	else {
		failureMessage = action.result.error;
	}

	Ext.MessageBox.show( {
		title :'Error',
		msg :failureMessage,
		buttons :Ext.MessageBox.OK,
		icon :Ext.MessageBox.ERROR
	});

} // submitFailed


function selectTreeFirst() {
	var storeCount = treecombo.store.getCount();

	if (storeCount > 0) {

		var rec = treecombo.store.getAt(0);

		treecombo.setValue(rec.get('id'));
		treecombo.fireEvent('select');
	} else {
		treecombo.setValue("");
		treecombo.fireEvent('select');
	}
} // end selectTreeFirst




function copySourcePanel(alias) {
	// e.stopEvent();
	var config = {
		width :600,
		height :300,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Copy Source ' + alias.get('sourceName')
	};

	var dialog = new Ext.Window(config);

	dialog.addButton('Close', dialog.close, dialog);

	var aliasForm = new Ext.form.FormPanel( {
		labelWidth :95,
		url :'do?action=createSource',
		baseCls :'x-plain',
		onSubmit :Ext.emptyFn
	});

	copyAliasDriverCombo = new Ext.form.ComboBox( {
		fieldLabel :'Driver Name',
		store :driversDataStore,
		displayField :'drvname',
		valueField :'id',
		mode :'local',

		editable :false,
		triggerAction :'all',
		selectOnFocus :true,
		width :400,
		forceSelection :true
	// ,
			// tpl:aliasTemplate
			});

	jdbcurl = new Ext.form.TextField( {
		fieldLabel :'JDBC URL',
		name :'url',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :alias.get('jdbcUrl')
	});
	copyAliasDriverCombo.on('select', function(cmb, rc, idx) {
		jdbcurl.setValue(rc.get('exampleurl'));
	});
	copyAliasDriverCombo.setValue(alias.get('driverid'));

	aliasForm.add(new Ext.form.TextField( {
		fieldLabel :'Source Name',
		name :'source',
		width :400,
		readOnly :false,
		allowBlank :false,
		value :alias.get('sourceName')
	}), copyAliasDriverCombo, jdbcurl, new Ext.form.TextField( {
		fieldLabel :'User Name',
		name :'user',
		width :400,
		readOnly :false,
		allowBlank :true,
		value :alias.get('userName')
	}));

	function onAliasCreateCopySubmit() {
		if (aliasForm.form.isValid()) {
			aliasForm.form.submit( {
				params : {
					driverid :copyAliasDriverCombo.getValue()
				},
				waitMsg :'Creating Source...',
				failure :submitFailed,
				success :submitSuccessful
			});
		} else {
			Ext.MessageBox.show( {
				title :'Error',
				msg :'Please fix the errors noted.',
				buttons :Ext.MessageBox.OK,
				icon :Ext.MessageBox.ERROR
			});
		}
	}
	function submitSuccessful(form, action) {
		databasesDataStore.reload();
		dialog.close();
	}
	dialog.addButton('Create Source', onAliasCreateCopySubmit, dialog);

	dialog.show();
	aliasForm.render(dialog.body);
}// End of copyAliasPanel


function createSourceMenu() {

	var sourcemenu = new Ext.menu.Menu(
	{

		shadow :'frame',
		id :'sourceMenu',
		items : [
				new Ext.menu.Item( {
					text :'Connect...',
					icon :'icons/database_lightning.png',
					handler : function() {
						openConnectionDialog(sourcesDataStore
								.getAt(sourcemenu.rowid));
					}
				}),
				new Ext.menu.Item( {
					text :'Edit...',
					icon :'icons/transmit_edit.png',
					handler : function() {
						editsourcesPanel(sourcesDataStore
								.getAt(sourcemenu.rowid));
					}
				}),
				
				new Ext.menu.Item( {
					text :'Copy Source...',
					icon :'icons/transmit_go.png',
					handler : function() {
					copySourcePanel(sourcesDataStore
								.getAt(sourcemenu.rowid));
					}
				}),

				new Ext.menu.Item(
				{
					text :'Delete...',
					icon :'icons/transmit_delete.png',
					handler : function() {
						function showResult(btn) {
							if (btn == 'yes') {
								new Ext.data.Connection()
										.request( {
											url :'do?action=deleteSource&id=' + sourcesDataStore
													.getAt(
															sourcemenu.rowid)
													.get('id'),
											method :'post',
											scope :this,
											params : {},
											callback : function(
													options,
													bSuccess,
													response) {
												sourcesDataStore
														.reload();
											}
										});
							}
						}
						;
						Ext.MessageBox
								.confirm(
										'Confirm',
										'Are you sure you want to delete source ' + sourcesDataStore
												.getAt(
														sourcemenu.rowid)
												.get(
														'sourceName') + ' ?',
										showResult);

					}
				}) ]
			});
	return sourcemenu;
} // createSourceMenu


function createDriverMenu() {

	var drivermenu = new Ext.menu.Menu(
	{

		shadow :'frame',
		id :'driverMenu',
		items : [
				
				new Ext.menu.Item( {
					text :'Edit...',
					icon :'icons/application_edit.png',
					handler : function() {
						editdriversPanel(driversDataStore
								.getAt(drivermenu.rowid));
					}
				}),
				

				new Ext.menu.Item(
				{
					text :'Delete...',
					icon :'icons/application_delete.png',
					handler : function() {
						function showResult(btn) {
							if (btn == 'yes') {
								new Ext.data.Connection()
										.request( {
											url :'do?action=deleteDriver&id=' + driversDataStore
													.getAt(
															drivermenu.rowid)
													.get('id'),
											method :'post',
											scope :this,
											params : {},
											callback : function(
													options,
													bSuccess,
													response) {
												driversDataStore
														.reload();
											}
										});
							}
						}
						;
						Ext.MessageBox
								.confirm(
										'Confirm',
										'Are you sure you want to delete driver ' + driversDataStore
												.getAt(
														drivermenu.rowid)
												.get(
														'driverName') + ' ?',
										showResult);

					}
				}) ]
			});
	return drivermenu;
} // createdriverMenu

function EditorPage(text) {

	var logPanel = new Logger();

	var sqlresultpanel = new Ext.TabPanel( {

		region :'center',

		tabPosition :'top',
		border :false,
		autoScroll :true,
		items : [ logPanel ],

		deferredRender :true,
		logPanel :logPanel
	});
	sqlresultpanel.activate(logPanel);
	sqlresultpanel.on('remove', function(container, comp) {
		if (comp.queryID) {

			new Ext.data.Connection().request( {
				url :'do?action=closeResultSet&rset=' + comp.queryID,
				method :'post',
				scope :this,
				params :this.baseParams
			});
		}
	});

//	var codePressEditor = new Ext.ux.CodePress( {
//		language :'sql',
//		lineNumbers :false,
//		autoComplete :false,
//		width :1200,
//		height :600,
//		border :false
//	});
	
	var codePressEditor=new SqlEditor({border :false,width :1200,height :1200,minHeight :1200,initialText: text});

	if (text) {
		//codePressEditor.setValue(text);
		editAreaLoader.setValue(codePressEditor.id, text)
	}

	var toolbar = createSQLToolbar(codePressEditor, sqlresultpanel);
	
	var sqlEditorPanel = new Ext.Panel( {
		tbar :toolbar,
		border :false,
		layout :'fit',
		items : [ codePressEditor ]
		
	});

	var editorPage = new Ext.Panel( {
		sqlresultpanel :sqlresultpanel,
		//title :'SQL Editor ' + nextCounter(),
		title :"<img src='icons/page_edit.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;SQL Editor " + nextCounter(),
		layout :"border",
		closable :true,
		border :false,
		toolbar:toolbar,
		execute: function(){this.toolbar.executeSQL()},
		hideMode: 'offsets',
		items : [ {
			region :'north',
			height :400,
			layout :'fit',
			split :true,
			autoScroll :true,
			items : [ sqlEditorPanel ]
		}, {
			region :'center',
			split :true,
			layout :'fit',
			items : [ sqlresultpanel ]
		} ]

	});
	editorPage.tb=toolbar;
	toolbar.editorPage = editorPage;
	return editorPage;
} // end EditorPage


function createSQLToolbar(codepressEditor, sqlresultpanel) {

	var clearBtn = new Ext.Toolbar.Button( {
		cls :'x-btn-icon',
		icon :'icons/page_white.png',
		tooltip :'<b>Clear</b><br/>Clear the Editor',
		// textareaid:textareaid,
		handler : function() {
			//codepressEditor.setCode("");
			editAreaLoader.setValue(codepressEditor.id, " ")
		}
	});


	var combo = new Ext.form.ComboBox( {
		store :databasesDataStore,
		displayField :'name',
		valueField :'id',
		typeAhead :false,
		editable :false,
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :135,
		forceSelection :true
	});

	var catalogStore = new Ext.data.SimpleStore( {
		fields : [ 'text' ],
		data : [ [] ]
	});
	var catalogCombo = new Ext.form.ComboBox( {
		store :catalogStore,
		displayField :'text',
		valueField :'text',
		typeAhead :false,
		editable :false,
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :135,
		forceSelection :true,
		disabled :true
	});

	function selectFirst() {
		if(combo.store==null)
			return;
		var storeCount = combo.store.getCount();
		if (storeCount > 0) {
			var oldValue = combo.getValue();
			if (oldValue && oldValue != "") {
				if (combo.store.query('id', oldValue).getCount() > 0) {
				} else {
					var rec = combo.store.getAt(0);
					combo.setValue(rec.get('id'));
				}
			} else {

				var rec = combo.store.getAt(0);
				combo.setValue(rec.get('id'));
			}
			combo.fireEvent('select');
			if (combo.run) {
				combo.run.enable();
			}
		} else {
			combo.setValue("");
			combo.fireEvent('select');
			if (combo.run) {
				try {
					combo.run.disable();
				} catch (e) {
				}
			}
		}

	}
	combo.store.on('load', function() {
		selectFirst();
	});

	var commitBtn = new Ext.Toolbar.Button( {
		cls :'x-btn-icon',
		tooltip :'<b>Commit</b>',
		icon :'icons/database_save.png',
		handler : function() {
			commitConnectionImpl(combo.getValue());
		}
	});
	commitBtn.disable();
	var rollbackBtn = new Ext.Toolbar.Button( {
		cls :'x-btn-icon',
		tooltip :'<b>Rollback</b>',
		icon :'icons/arrow_undo.png',
		handler : function() {
			rollbackConnectionImpl(combo.getValue());
		}
	});
	rollbackBtn.disable();
	function enableDisableCatalogs() {
		var sessionid = combo.getValue();
		if (sessionid) {
			var selectedConnection = databasesDataStore.getById(sessionid);
			if (selectedConnection) {
				if (selectedConnection.get('autocommit') == false) {
					commitBtn.enable();
					rollbackBtn.enable();
				}else{
					commitBtn.disable();
					rollbackBtn.disable();
				}
				var hasCatalogs = selectedConnection.get('hasCatalogs');
				if (hasCatalogs) {
					var catalogs = selectedConnection.get('catalogs');
					var rta = [];
					for (i = 0; i < catalogs.length; i++) {
						var rta2 = [];
						rta2.push(catalogs[i]);
						rta.push(rta2);
					}
					catalogStore.loadData(rta);
					catalogCombo.enable();
					catalogCombo.setValue(selectedConnection.get('catalog'));
				} else {
					if (catalogCombo.el) {
						catalogCombo.clearValue();
					}
					catalogCombo.disable();
				}
			} else {
				if (catalogCombo.el) {
					catalogCombo.clearValue();
				}
				catalogCombo.disable();

			}
		} else {
			commitBtn.disable();
			rollbackBtn.disable();
			try {
				if (catalogCombo.el) {
					catalogCombo.clearValue();
				}
				catalogCombo.disable();
			} catch (e) {
			}

		}
	}

	combo.on('select', function(combo, record, index) {
		enableDisableCatalogs();
	})

	catalogCombo.on('select', function() {
		var sessionid = combo.getValue();
		var selectedConnection = databasesDataStore.getById(sessionid);
		selectedConnection.set('catalog', catalogCombo.getValue());
		new Ext.data.Connection().request( {
			url :'do?action=changeCatalog',
			method :'post',
			params : {
				catalog :catalogCombo.getValue(),
				sessionid :sessionid
			}

		});
	});

	combo.setValue(treecombo.getValue());
	enableDisableCatalogs();
	function executeSQL(){
		var code = "";
		code=editAreaLoader.getValue(codepressEditor.id);
//		if (codepressEditor.textarea && codepressEditor.editor)
//			code = codepressEditor.textarea.dom.disabled ? codepressEditor.editor
//					.getCode()
//					: codepressEditor.textarea.dom.value;
//		else
//			code = codepressEditor.code || "";
		sqlresultpanel.logPanel.info2('Starting to execute', code);
		new Ext.data.Connection().request( {
			url :'do?action=execute',
			method :'post',
			timeout :'3600000',
			params : {
				sql :code,
				sessionid :combo.getValue()
			},
			failure :requestFailed,
			success :sqlSuccessful,
			sqlresultpanel :sqlresultpanel
		});
	}
	var runBtn = new Ext.Toolbar.Button({
		cls :'x-btn-icon',
		icon :'icons/lightning.png',
		tooltip :'<b>Run SQL</b><br/>Execute SQL text on server',
		handler : function() {
		 	executeSQL();
		}
	});
//	
	var syntaxBtn = new Ext.Toolbar.Button({
		cls :'x-btn-icon',
		icon :'icons/lightbulb.png',
		tooltip :'<b>HighLight</b><br/>Syntax HighLight',
		enableToggle :true,
		pressed :true,
		handler : function() {
			editAreaLoader.toggle(codepressEditor.id);
		}
	});

	// var centerToolbar=new Ext.Toolbar({
	// items:[clearBtn,openBookmark,saveBookmark,{xtype:
	// 'tbseparator'},combo,catalogCombo,{xtype: 'tbseparator'},runBtn,{xtype:
	// 'tbseparator'},commitBtn,rollbackBtn]});
	var centerToolbar = new Ext.Toolbar( {
		executeSQL:executeSQL,
		listeners : {
			render : function() {
				this.addButton(clearBtn);
				this.addButton(syntaxBtn);
//				this.addButton(openBookmarkBtn);
//				openBookmarkBtn.editorPage = this.editorPage;
//				this.addButton(saveBookmark);
				this.addSeparator();
				this.add(combo);
				this.add(catalogCombo);
				this.addSeparator();
				this.addButton(runBtn);
				this.addSeparator();
				this.addButton(commitBtn);
				this.addButton(rollbackBtn);
			}
		}
	});

//	combo.run = runBtn;
//	var storeCount = combo.store.getCount();
//	if (storeCount == 0) {
//		combo.run.disable();
//	}
	centerToolbar.cmb=combo;
	centerToolbar.catcmb=catalogCombo;
	return centerToolbar;
}// end createSQLToolbar

function sqlSuccessful(response, options) {
	sqlresultpanel = options.sqlresultpanel;
	sqlresultpanel.logPanel.info('SQL Executed. Getting Response...');
	var object = Ext.util.JSON.decode(response.responseText);
	if (object.success) {
		resultSets = object.result.resultSets;
		info = object.result.info;
		sqlresultpanel.logPanel.info('SQL Executed. Execution time: ' + info[0]
				+ ' milliseconds. Executed statements: ' + resultSets.length);
		for (ii = 0; ii < resultSets.length; ii++) {
			var meta = object.result.resultSets[ii].meta;
			var myData = object.result.resultSets[ii].data;
			var info2 = object.result.resultSets[ii].info;
			var queryID = info2[0];
			var executed = info2[2];
			if (!executed) {
				sqlresultpanel.logPanel.error('\"' + info2[3]
						+ '\" executing: ' + info2[1]);
				sqlresultpanel.setActiveTab(sqlresultpanel.logPanel);
			} else {

				var resultGrid = createTableGrid(queryID, meta, myData);

				var resultPanels = [];
				resultPanels.push(resultGrid);

				for ( var j = 0; j < sqlResultPluginFactories.length; j++) {
					var panel = sqlResultPluginFactories[j].build(queryID,
							meta, myData);
					resultPanels.push(panel);
				}

				var next = new Ext.Toolbar.Button( {
					cls :'x-btn-icon',
					icon :'icons/resultset_next.png',
					tooltip :'<b>Next </b><br/>Get Additional Records',
					queryID :queryID,
					myData :myData,
					resultPanels :resultPanels,
					handler : function() {
						this.disable();
						new Ext.data.Connection().request( {
							url :'do?action=getAdditionalData',
							method :'post',
							params : {
								queryID :this.queryID
							},
							failure :requestFailed,
							success :sqlAdditionalSuccessful,
							sqlresultpanel :sqlresultpanel,
							myData :this.myData,
							next :this,
							resultPanels :this.resultPanels
						});
					}
				});
				var nextAll = new Ext.Toolbar.Button( {
					cls :'x-btn-icon',
					icon :'icons/resultset_next2.png',
					tooltip :'<b>Retrieve All </b><br/>Retrieve All Additional Records',
					queryID :queryID,
					myData :myData,
					resultPanels :resultPanels,
					handler : function() {
						this.disable();
						new Ext.data.Connection().request( {
							url :'do?action=getAdditionalData',
							method :'post',
							params : {
								all: 1,
								queryID :this.queryID
							},
							failure :requestFailed,
							success :sqlAdditionalSuccessfulAll,
							sqlresultpanel :sqlresultpanel,
							myData :this.myData,
							nextAll :this,
							next:next,
							resultPanels :this.resultPanels
						});
					}
				});
				var refresh = new Ext.Toolbar.Button(
				{
					cls :'x-btn-icon',
					icon :'icons/arrow_refresh.png',
					tooltip :'<b>Refresh </b><br/>Execute Again',
					queryID :queryID,
					myData :myData,
					resultPanels :resultPanels,
					handler:function(){
						new Ext.data.Connection().request( {
							url :'do?action=redoQuery',
							method :'post',
							params : {
								queryID :this.queryID
							},
							failure :requestFailed,
							success :sqlRefreshSuccessful,
							sqlresultpanel :sqlresultpanel,
							myData :this.myData,
							nextAll :nextAll,
							next:next,
							resultPanels :this.resultPanels
						});
					}
				});

				var clLayout = new Ext.layout.CardLayout( {
					deferredRender :true,
					layoutOnCardChange:true
				});

				var menu_chooseSQLLayout = new Ext.menu.Menu( {});

				for ( var i = 0; i < resultPanels.length; i++) {
					var tobechecked = preferredLayout == i ? true : false;
					var name = "Grid";
					if (i > 0) {
						name = sqlResultPluginFactories[i - 1].name();
					}
					menu_chooseSQLLayout.addMenuItem(new Ext.menu.CheckItem( {
						sel :i,
						text :name,
						activePanel :resultPanels[i],
						checked :tobechecked,
						group :'sqllayout',
						clLayout :clLayout,
						checkHandler :onLayoutCheck
					}));
				}

				var printButton = new Ext.Toolbar.Button(
				{
					cls :'x-btn-icon',
					icon :'icons/printer.png',
					tooltip :'<b>Print </b><br/>Printable Version ',
					handler : function() {
						if (printingWindow && printingWindow.open && !printingWindow.closed) 
							printingWindow.close();
						printingWindow = window.open('','p','resizable=yes,width=600,height=400,toolbar=yes,scrollbars=yes,modal=yes');
				    
						printingWindow.document.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type" /><link rel="stylesheet" type="text/css" href="print.css"/> <title>Printing...</title></head><body><input onclick="this.style.visibility=\'hidden\';window.print()" type="button" value="Print"/><div id="printingDiv"></div></body></html>');
						printingWindow.document.close();
						
						
						var tmpObject=new Object();
		        		tmpObject["data"]=myData;
		        		tmpObject["meta"]=meta;
		        		sqlPrintingTableTemplate.overwrite(printingWindow.document.getElementById("printingDiv") , tmpObject, true);
						
					}
				});
                                
				var exportCSVButton=new Ext.Toolbar.Button({
					cls :'x-btn-icon',
					icon :'icons/database_table.png',
					tooltip :'<b>Export</b><br/>Export to CSV',
					handler : function() {
						if (!Ext.fly('frmCsvDummy')) {
                                                    var frm = document.createElement('form');
                                                    frm.id = 'frmCsvDummy';
                                                    frm.name = id;
                                                    frm.className = 'x-hidden';
                                                    frm.target='_blank';
                                                    document.body.appendChild(frm);
                                                }
                                                Ext.Ajax.request({
                                                    url: 'do?action=csvExport',
                                                    method : 'POST',
                                                    form: Ext.fly('frmCsvDummy'),
                                                    isUpload:true,
                                                    params: {action:'csvExport',meta:Ext.util.JSON.encode(meta),data:Ext.util.JSON.encode(myData),info:Ext.util.JSON.encode(info2)}
                                                });
					}
				});

                                var exportPDFButton=new Ext.Toolbar.Button({
					cls :'x-btn-icon',
					icon :'icons/page_white_acrobat.png',
					tooltip :'<b>Export</b><br/>Export to PDF',
					handler : function() {
						if (!Ext.fly('frmPdfDummy')) {
                                                    var frm = document.createElement('form');
                                                    frm.id = 'frmPdfDummy';
                                                    frm.name = id;
                                                    frm.className = 'x-hidden';
                                                    frm.target='_blank';
                                                    document.body.appendChild(frm);
                                                }
                                                Ext.Ajax.request({
                                                    url: 'do?action=pdfExport',
                                                    method : 'POST',
                                                    form: Ext.fly('frmPdfDummy'),
                                                    isUpload:true,
                                                    params: {action:'pdfExport',meta:Ext.util.JSON.encode(meta),data:Ext.util.JSON.encode(myData),info:Ext.util.JSON.encode(info2)}
                                                });
					}
				});

				var exportExcelButton=new Ext.Toolbar.Button({
					cls :'x-btn-icon',
					icon :'icons/page_white_excel.png',
					tooltip :'<b>Export</b><br/>Export to Excel',
					handler : function() {				
						var exportContent=resultGrid.getExcelXml(true);
						if (Ext.isGecko3) {
                                                    document.location='data:application/vnd.ms-excel;Content-Disposition:attachment;filename=export_filename.xls;name=export.xls;base64,' + Base64.encode(exportContent);
                                                }
						else{
							if (!Ext.fly('frmDummy')) {
                                                            var frm = document.createElement('form');
                                                            frm.id = 'frmDummy';
                                                            frm.name = id;
                                                            frm.className = 'x-hidden';
                                                            frm.target='_blank';
                                                            document.body.appendChild(frm);
                                                        }
                                                    Ext.Ajax.request({
                                                        url: 'do?action=excelExport',
                                                        method : 'POST',
                                                        form: Ext.fly('frmDummy'),
                                                        isUpload:true,
                                                        params: {action:'excelExport',ex: resultGrid.getExcelXml(true)}
                                                    });
						}
                    
					}
				});
				var clPanel = new Ext.Panel( {
					closable :true,
					title :"<img src='icons/table.png' style='vertical-align:bottom;height:16px;width:16px' >&nbsp;Results",
					autoScroll :true,
					border :false,
					items :resultPanels,
					layout :clLayout,
					activeItem :preferredLayout,
					tbar : [ next,nextAll, {
						xtype :'tbseparator'
					}, refresh, {
						xtype :'tbseparator'
					}, {
						cls :'x-btn-icon',
						tooltip :'<b>Layout</b><br/>Choose Layout',
						menu :menu_chooseSQLLayout,
						style :'width:20px',
						icon :"icons/layout.png"
					}, {
						xtype :'tbseparator'
					},printButton,{
						xtype :'tbseparator'
					},exportExcelButton,exportPDFButton,exportCSVButton ],
					queryID :queryID

				});

				sqlresultpanel.add(clPanel);
				sqlresultpanel.doLayout(false);
				sqlresultpanel.setActiveTab(clPanel);
			}
		}

	} else {
		sqlresultpanel.logPanel.error(object.error);
		Ext.MessageBox.show( {
			title :'Error',
			msg :object.error,
			buttons :Ext.MessageBox.OK,
			icon :Ext.MessageBox.ERROR
		});
	}
}// end sqlSuccessful

function sqlRefreshSuccessful(response, options){
	sqlresultpanel = options.sqlresultpanel;
	sqlresultpanel.logPanel.info('SQL Executed. Getting Response...');
	var object = Ext.util.JSON.decode(response.responseText);
	if (object.success) {
		options.next.enable();
		options.nextAll.enable();
		data = object.result.data;
		options.myData.clear();
		for ( var i = 0; i < data.length; i++) {
			options.myData.push(data[i]);
		}
		for ( var i = 0; i < options.resultPanels.length; i++) {
			if (options.resultPanels[i].refreshData) {
				options.resultPanels[i].refreshData(data);
			}
		}
	} else {
		sqlresultpanel.logPanel.error(object.error);
		Ext.MessageBox.show( {
			title :'Error',
			msg :object.error,
			buttons :Ext.MessageBox.OK,
			icon :Ext.MessageBox.ERROR
		});
	}
		
}//end sqlRefreshSuccessful

Logger = function() {
	var tpl = new Ext.Template(
			"<div class='x-log-entry'><div class='x-log-level x-log-{0:lowercase}'>"
					+ "{0:capitalize}</div><span class='x-log-time'>{2:date('H:i:s.u')}</span>"
					+ "<span class='x-log-message'>{1}</span></div>");
	var tpl2 = new Ext.Template(
			"<div class='x-log-entry'><div class='x-log-level x-log-{0:lowercase}'>"
					+ "{0:capitalize}</div><span class='x-log-time'>{2:date('H:i:s.u')}</span>"
					+ "<span class='x-log-message'>{1}</span><span style='color:red'><strong>{3}</strong></span></div>");

	return Ext.apply(new Ext.Panel( {
		title :"<img src='icons/application_xp_terminal.png' style='vertical-align:bottom;height:16px;width:16px' >&nbsp;Console",
		layout :'fit',
		border :false,
		autoScroll :true
	}), {
		fn :Ext.Template.prototype.insertFirst,

		toggleDirection : function() {
			if (this.fn === Ext.Template.prototype.insertFirst) {
				this.fn = Ext.Template.prototype.append;
			} else {
				this.fn = Ext.Template.prototype.insertFirst;
			}
		},

		debug : function(msg) {
			this.fn.call(tpl, this.body, [ 'debug', msg, new Date() ], true)
					.scrollIntoView(this.body);
		},

		info : function(msg) {
			this.fn.call(tpl, this.body, [ 'info', msg, new Date() ], true)
					.scrollIntoView(this.body);
		},

		info2 : function(msg, msg2) {
			this.fn.call(tpl2, this.body, [ 'info', msg, new Date(), msg2 ],
					true).scrollIntoView(this.body);
		},

		warning : function(msg) {
			this.fn.call(tpl, this.body, [ 'warning', msg, new Date() ], true)
					.scrollIntoView(this.body);
		},

		error : function(msg) {
			this.fn.call(tpl, this.body, [ 'error', msg, new Date() ], true)
					.scrollIntoView(this.body);
		}
	});
}

SqlEditor = Ext.extend(Ext.form.Field,{
	readOnly:false,
	initialized:false,
	initComponent : function(){
		SqlEditor.superclass.initComponent.call(this);
		this.addEvents({
          initialize: true,
          activate: true

	    });
	},
	focus : function(){},
	adjustSize : Ext.BoxComponent.prototype.adjustSize,
	
	onResize : function(w, h){
		SqlEditor.superclass.onResize.apply(this, arguments);
		
        if(this.el && this.wrap && this.textarea){
            if(typeof h == 'number' && typeof w == 'number'){
                
                this.textarea.dom.style.height=h + 'px';
                this.textarea.dom.style.width=w + 'px';
                var frame = document.getElementById("frame_"+this.textarea.id); 
                //alert("frame "+this.textarea.id+ " " +frame);
                if (frame!=null) { 
	                frame.style.height = h + 'px'; 
	                frame.style.width = w + 'px';  
	                var area = window.frames["frame_" + this.textarea.id].editArea;
	                if(area)
	                	area.execCommand("update_size");  
                } 
                
            }
        }
    },
    onDestroy : function(){
        if(this.rendered){
            this.wrap.dom.innerHTML = '';
            this.wrap.remove();
        }
    },
	onRender : function(ct, position){
		SqlEditor.superclass.onRender.call(this, ct, position);
		this.el.dom.style.border = '0 none';
        this.el.dom.setAttribute('tabIndex', -1);
        this.el.addClass('x-hidden');
    
    
        if(Ext.isIE){ // fix IE 1px bogus margin
            this.el.applyStyles('margin-top:-1px;margin-bottom:-1px;')
        }
        this.wrap = this.el.wrap({height:'100%'});
        this.textarea = Ext.get(document.createElement('textarea'));
        this.textarea.dom.disabled = false;
        //this.textarea.dom.style.overflow = 'hidden';
        this.textarea.dom.style.overflow = 'auto';
        //style="height: 350px; width: 100%;"
        this.textarea.dom.style.height='350px';
        this.textarea.dom.style.width='100%';
        this.options = this.textarea.dom.className;    
        this.wrap.dom.appendChild(this.textarea.dom);
        this.id=this.textarea.id;
        editAreaLoader.init({
        	allow_toggle: false,
        	id : this.textarea.id		// textarea id
        	,syntax: "sql"			// syntax to be uses for highgliting
        	,start_highlight: true		// to display with highlight mode on start-up
        	,show_line_colors: true
        	,allow_resize: "no"
        	, toolbar:''
        	,autocompletion_start:false

        	//,toolbar:""
        });
        editAreaLoader.setValue(this.id, this.initialText);
/*
 * ,start_highlight: true
			,allow_toggle: false
			,language: "en"
			,syntax: "html"	
			,toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help"
			,syntax_selection_allow: "css,html,js,php,python,vb,xml,c,cpp,sql,basic,pas,brainfuck"
			,is_multi_files: true
			,EA_load_callback: "editAreaLoaded"
			,show_line_colors: true

 */
        
	},
	initialize: function(){
		this.initialized = true;
	    this.fireEvent('initialize', this);
	    
	}
        
	
});

function newEditor(text) {

	var editorPage = new EditorPage(text);
	var tabFolder = Ext.getCmp('tabpanelpages');

	tabFolder.add(editorPage);
	tabFolder.doLayout(false);
	tabFolder.activate(editorPage);
	return editorPage;
}// end newEditor


function createTree() {
	
	synchro = new Ext.Toolbar.Button( {
		tooltip :'<b>Link</b><br/>Link Tree with Database Details',
		icon :'icons/lock.png',
		enableToggle :true,
		pressed :true,
		cls :'x-btn-icon'
	});
	treecombo = new Ext.form.ComboBox( {
		store :databasesDataStore,
		displayField :'name',
		valueField :'id',
		typeAhead :false,
		editable :false,
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :135,
		forceSelection :true,
		emptyText:'No connections'
	});

	treecombo.on('select', function(cmb, rc, idx) {

		if (tree && tree.root)
			tree.root.reload();

	});

	treecombo.store.on('load', function() {
		selectTreeFirst();
	});

	var treeStore = new Ext.data.Store( {
		proxy :httpTreeProxy,
		reader :treeReader
	});

	treeStore.on('loadexception', function(){});
	
	var rootOptions = {
		id :"root",
		text :'blabla'
	};

	var root = new Ext.tree.AsyncTreeNode(rootOptions);

	var treeLoader = new TreeStoreLoader( {
		store :treeStore,
		nodeParamName :"node"
	});

	rootOptions = Ext.applyIf(rootOptions || {}, {
		loader :treeLoader
	});
	var newEditorBtn = new Ext.Toolbar.Button( {
		tooltip :'<b>New Editor</b><br/>Open a new SQL Editor',
		icon :'icons/page_edit.png',
		cls :'x-btn-icon',
		handler : function() {
			newEditor("");
		}
	}
	
	);
	
	var newRelantionshipsBtn = new Ext.Toolbar.Button(	{
		tooltip :'<b>New Tables Viewer</b><br/>Open a new Tables Viewer',
		icon :'icons/table_relationship.png',
		cls :'x-btn-icon',
		handler : function() {
			new RelViewer();
		}
	});
	
	
	var root = new Ext.tree.AsyncTreeNode(rootOptions);
	var tree = new Ext.tree.TreePanel( {
		border :false,
		tbar : [synchro,{xtype: 'tbseparator'}, treecombo,{xtype: 'tbseparator'},newEditorBtn,newRelantionshipsBtn],
		title:"",//<img src='icons/database.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Database View",
		animate :true,
		enableDD :false,
		// loader: myTreeLoader,
		lines :true,
		rootVisible :false,
		selModel :new Ext.tree.DefaultSelectionModel(),
		containerScroll :false,
		autoScroll :true,
		// root:root,
		enableDrag :true,
		ddGroup :'testDD',hideMode: 'offsets'
	});

	tree.setRootNode(root);

	myTreeLoader = treeLoader;

	myTreeLoader.on("beforeload", function(treeLoader, node) {
		myTreeLoader.baseParams.dbid = treecombo.getValue();
	}, this);

	var menuTreeC = new Ext.menu.Menu();//Ext.id());
	tree.on('contextmenu', function(node, e) {
		menuTreeC.removeAll();
		var tabKey = node.attributes.type + '-' + treecombo.getValue();
		dynMenuArray
		if (dynMenuArray[tabKey] != null) {
			//buildDynamicMenu(dynMenuArray[tabKey]);
			buildDynamicMenu(dynMenuArray[tabKey],menuTreeC);
			menuTreeC.nodeid = node;
			menuTreeC.showAt(e.getXY());
			
		} else {
			new Ext.data.Connection().request( {
				url :'do?action=getMenu&nodeType=' + node.attributes.type
						+ '&sessionid=' + treecombo.getValue(),
				method :'post',
				scope :this,
				params :this.baseParams,
				success :menuSuccess,
				menuTreeC :menuTreeC,
				event :e,
				node:node,
				tabKey :tabKey
			});
		}
	});
	treeselectionModel = tree.getSelectionModel();
	treeselectionModel.on('beforeselect', nodeSelection);
	return tree;
} // end createTree

function refreshNode(node){
	node.collapse(false, false);
	while (node.firstChild) {
		node.removeChild(node.firstChild);
	}
	node.childrenRendered = false;
	node.loaded = false;
	myTreeLoader.baseParams.refresh = true
	node.expand(false, false);
	myTreeLoader.baseParams.refresh = false;
}

function buildDynamicMenu(arr,menuTreeC){
	for ( var i = 0; i < arr.length; i++) {

		if(arr[i][0]=='-'){
			menuTreeC.add(new Ext.menu.Separator({}));
		}else{
			var newitem = new Ext.menu.Item( {
				text :arr[i][0],
				icon :arr[i][1]
			});
			newitem.iindex = i;
			newitem.on("click", function() {
				var script = arr[this.iindex][2];
				eval(script);
			}, this.scope, true);
			menuTreeC.add(newitem);
		}

	}
}

function menuSuccess(response, options){
	var object = Ext.decode(response.responseText);
	var menuTreeC = options.menuTreeC;
	var e = options.event;
	if (object.success) {
		var menur = object.result.menu;
		var arr = object.result.menu;
		dynMenuArray[options.tabKey] = menur;
		buildDynamicMenu(arr,menuTreeC);
		menuTreeC.nodeid = options.node;
		menuTreeC.showAt(e.getXY());
	}
}

function createScheduledPage(){
	createTabPage("do?action=scheduled","icons/date.png","Scheduled Jobs");
}

function createReportsPage(){
	createTabPage("do?action=reports","icons/report.png","Reports");
}

function createConfigurationPage(){
	createTabPage("do?action=config","icons/cog.png","Configuration");
}


function createAboutPage(){
	createTabPage("do?action=about","icons/information.png","About");
}
function createDataSourcesPage(){
	createTabPage("do?action=sourcesPage","icons/transmit.png","Sources");
}

function createCustomizeExport(){
	var tabFolder = Ext.getCmp('tabpanelpages');
        var filename = '';
        var csvseparator = '';
        var fileencoding = '';

        var importpage = new Ext.Panel({
            autoScroll :true,
            closable:true,
            title :"<img src='cd_edit.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Export"
	});

        var connectionCombo = new Ext.form.ComboBox( {
		store :databasesDataStore,
                fieldLabel: 'Connection',
		displayField :'name',
		valueField :'id',
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :200,
		forceSelection :true
	});

        connectionCombo.on('select', function() {
            var selectedConnection = databasesDataStore.getById(connectionCombo.getValue());
            var catalogs = selectedConnection.get('catalogs');
            var rta = [];
            for (i = 0; i < catalogs.length; i++) {
                    var rta2 = [];
                    rta2.push(catalogs[i]);
                    rta.push(rta2);
            }
            databaseStore.loadData(rta,false);
        });

        var databaseStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: ['databasename']
        });

        var databaseCombo = new Ext.form.ComboBox( {
		store :databaseStore,
                fieldLabel: 'Database',
		displayField :'databasename',
		valueField :'databasename',
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :200,
		forceSelection :true
	});

        databaseCombo.on('select', function() {
            var tableComboReader = new Ext.data.JsonReader({
                root: 'result.tables'
                },['tablename']);

            var tableComboProxy = new Ext.data.HttpProxy({
                   url: 'request?class=DatabaseInfo&method=getTables&id='+connectionCombo.getValue()+'&catalogName='+databaseCombo.getValue(),
                   method : "POST"
                });

            var tableComboDataStore = new Ext.data.Store({
                proxy: tableComboProxy,
                reader: tableComboReader
            });

            tableComboDataStore.on('load', loadTablesSuccessful);
            function loadTablesSuccessful() {
                var rta = [];
                for (i = 0; i < tableComboDataStore.getCount(); i++) {
                        var rta2 = [];
                        rta2.push(tableComboDataStore.getAt(i).get('tablename'));
                        rta.push(rta2);
                }
                tableStore.loadData(rta,false);
            }

            tableComboDataStore.load();

        });

        var tableStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: ['tablename']
        });

	var tableCombo = new Ext.form.ComboBox({
            store :tableStore,
            fieldLabel: 'Table',
            displayField :'tablename',
            valueField :'tablename',
            mode :'local',
            triggerAction :'all',
            selectOnFocus :true,
            width :200,
            forceSelection :true
	});

        tableCombo.on('select', function() {
            var columnSelectorReader = new Ext.data.JsonReader({
                root: 'result.columns'
                },['cname']);

            var columnSelectorProxy = new Ext.data.HttpProxy({
                   url: 'request?class=DatabaseInfo&method=getColumns&id='+connectionCombo.getValue()+'&catalogName='+databaseCombo.getValue()+'&tableName='+tableCombo.getValue(),
                   method : "POST"
                });

            var columnSelectorDataStore = new Ext.data.Store({
                proxy: columnSelectorProxy,
                reader: columnSelectorReader
            });

            columnSelectorDataStore.on('load', loadColumnsSuccessful);
            function loadColumnsSuccessful() {
                var rta = [];
                for (i = 0; i < columnSelectorDataStore.getCount(); i++) {
                        var rta2 = [];
                        rta2.push(columnSelectorDataStore.getAt(i).get('cname'));
                        rta.push(rta2);
                }
                Ext.getCmp("columnSelector").fromStore.loadData(rta,false);
                Ext.getCmp("columnSelector").toStore.removeAll();
            }

            columnSelectorDataStore.load();

        });


        var columnSelectorDef = Ext.data.Record.create( [ {
            name :'cname',
            type :'string'
        } ]);

        var columnStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: columnSelectorDef,
            sortInfo: {
                field: 'cname',
                direction: 'ASC'
            }
        });

        var columnSelector = new Ext.ux.ItemSelector( {
            name :"columnSelector",
            id : "columnSelector",
            fieldLabel : "Columns",
            dataFields : ['cname'],
            toData : [[]],
            msWidth :150,
            msHeight :350,
            valueField :"cname",
            displayField :"cname",
            imagePath: 'icons/images/',
            toLegend :"Selected",
            fromLegend :"Available",
            fromStore :columnStore
    });


        var exportFormPanel = new Ext.FormPanel({
            fileUpload: true,
            width: 500,
            title: 'Export Tables',
            bodyStyle: 'padding: 10px 10px 0 10px;',
            labelWidth: 150,
            items: [{
                    xtype:          'combo',
                    name:           'separator',
                    id:           'separator',
                    fieldLabel:     'Column Separator',
                    value:          'comma',
                    typeAhead: true,
                    triggerAction: 'all',
                    mode: 'local',
                    forceSelection: true,
                    displayField:   'name',
                    valueField:     'value',
                    store:          new Ext.data.ArrayStore({
                        fields : ['value', 'name'],
                        data   : [
                            ['comma',   'Comma'],
                            ['tab',  'Tab'],
                            ['verticalbar', 'Vertical Bar']
                        ]
                    })

                },
                {
                    xtype:          'combo',
                    name:           'fileencoding',
                    id:           'fileencoding',
                    fieldLabel:     'File Encoding',
                    value:          'ascii',
                    triggerAction: 'all',
                    mode: 'local',
                    forceSelection: true,
                    displayField:   'name',
                    valueField:     'value',
                    store:          new Ext.data.ArrayStore({
                        fields : ['value', 'name'],
                        data   : [
                            ['ascii',   'ASCII'],
                            ['utf8',  'UTF-8'],
                            ['unicode', 'Unicode']
                        ]
                    })

                },connectionCombo,databaseCombo,tableCombo,columnSelector],
            buttons: [{
                text: 'Export',
                handler: function(){
                    if(exportFormPanel.getForm().isValid()){
                            exportFormPanel.getForm().submit({
                                url: 'do?action=export',
                                waitMsg: 'Exporting...',
                                success: function(result, request){
                                    alert("Success"+csvseparator+fileencoding);
                                }
                            });
                    }
                }
            },{
                text: 'Reset',
                handler: function(){
                    exportFormPanel.getForm().reset();
                }
            }]
        });

        importpage.add(exportFormPanel);
	tabFolder.add(importpage);
	tabFolder.setActiveTab(importpage);
}

function createCustomizeImport(){
	var tabFolder = Ext.getCmp('tabpanelpages');
        var filename = '';
        var csvseparator = '';
        var fileencoding = '';

        var importpage = new Ext.Panel({
            autoScroll :true,
            closable:true,
            title :"<img src='cd_edit.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Import"
	});

        var uploadstep1 = new Ext.ux.form.FileUploadField({
            allowBlank: false,
            id: 'uploadstep1',
            emptyText: 'Select a CSV File',
            width: 300,
            fieldLabel: 'CSV File',
            name: 'csvfile',
            buttonText: '',
            buttonCfg: {
                iconCls: 'upload-icon'
            }
        });

        var fileUploadPanel1 = new Ext.FormPanel({
            fileUpload: true,
            width: 500,
            title: 'Import Step 1',
            bodyStyle: 'padding: 10px 10px 0 10px;',
            labelWidth: 150,
            items: [
                {
                    xtype:          'combo',
                    name:           'separator',
                    id:           'separator',
                    fieldLabel:     'Column Separator',
                    value:          'comma',
                    typeAhead: true,
                    triggerAction: 'all',
                    mode: 'local',
                    forceSelection: true,
                    displayField:   'name',
                    valueField:     'value',
                    store:          new Ext.data.ArrayStore({
                        fields : ['value', 'name'],
                        data   : [
                            ['comma',   'Comma'],
                            ['tab',  'Tab'],
                            ['verticalbar', 'Vertical Bar']
                        ]
                    })

                },
                {
                    xtype:          'combo',
                    name:           'fileencoding',
                    id:           'fileencoding',
                    fieldLabel:     'File Encoding',
                    value:          'ascii',
                    triggerAction: 'all',
                    mode: 'local',
                    forceSelection: true,
                    displayField:   'name',
                    valueField:     'value',
                    store:          new Ext.data.ArrayStore({
                        fields : ['value', 'name'],
                        data   : [
                            ['ascii',   'ASCII'],
                            ['utf8',  'UTF-8'],
                            ['unicode', 'Unicode']
                        ]
                    })

                },
                uploadstep1],
            buttons: [{
                text: 'Next...',
                handler: function(){
                    if(fileUploadPanel1.getForm().isValid()){
                            fileUploadPanel1.getForm().submit({
                                url: 'do?action=import',
                                waitMsg: 'Uploading your file...',
                                success: function(result, request){
                                    var jsonobject = Ext.util.JSON.decode(request.response.responseText);
                                    filename = jsonobject.result.filename;
                                    csvseparator = Ext.getCmp("separator").getValue();
                                    fileencoding = Ext.getCmp("fileencoding").getValue();
                                    //alert("Success"+jsonobject.result.filename);
                                    fileUploadPanel1.hide();
                                    fileUploadPanel2.show();

                                },
                                failure: function(result, request)
                                {
                                    var jsonobject = Ext.util.JSON.decode(request.response.responseText);
                                    alert("upload failed:"+ jsonobject.error);
                                }
                            });
                    }
                }
            },{
                text: 'Reset',
                handler: function(){
                    fileUploadPanel1.getForm().reset();
                }
            }]
        });

        var connectionCombo = new Ext.form.ComboBox( {
		store :databasesDataStore,
                fieldLabel: 'Connection',
		displayField :'name',
		valueField :'id',
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :200,
		forceSelection :true
	});
        
        connectionCombo.on('select', function() {
            var selectedConnection = databasesDataStore.getById(connectionCombo.getValue());
            var catalogs = selectedConnection.get('catalogs');
            var rta = [];
            for (i = 0; i < catalogs.length; i++) {
                    var rta2 = [];
                    rta2.push(catalogs[i]);
                    rta.push(rta2);
            }
            databaseStore.loadData(rta,false);
        });

        var databaseStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: ['databasename']
        });

        var databaseCombo = new Ext.form.ComboBox( {
		store :databaseStore,
                fieldLabel: 'Database',
		displayField :'databasename',
		valueField :'databasename',
		mode :'local',
		triggerAction :'all',
		selectOnFocus :true,
		width :200,
		forceSelection :true
	});

        databaseCombo.on('select', function() {
            var tableComboReader = new Ext.data.JsonReader({
                root: 'result.tables'
                },['tablename']);

            var tableComboProxy = new Ext.data.HttpProxy({
                   url: 'request?class=DatabaseInfo&method=getTables&id='+connectionCombo.getValue()+'&catalogName='+databaseCombo.getValue(),
                   method : "POST"
                });

            var tableComboDataStore = new Ext.data.Store({
                proxy: tableComboProxy,
                reader: tableComboReader
            });

            tableComboDataStore.on('load', loadTablesSuccessful);
            function loadTablesSuccessful() {
                var rta = [];
                for (i = 0; i < tableComboDataStore.getCount(); i++) {
                        var rta2 = [];
                        rta2.push(tableComboDataStore.getAt(i).get('tablename'));
                        rta.push(rta2);
                }
                tableStore.loadData(rta,false);
            }

            tableComboDataStore.load();
            
        });

        var tableStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: ['tablename']
        });
        
	var tableCombo = new Ext.form.ComboBox({
            store :tableStore,
            fieldLabel: 'Table',
            displayField :'tablename',
            valueField :'tablename',
            mode :'local',
            triggerAction :'all',
            selectOnFocus :true,
            width :200,
            forceSelection :true
	});

        tableCombo.on('select', function() {
            var columnSelectorReader = new Ext.data.JsonReader({
                root: 'result.columns'
                },['cname']);

            var columnSelectorProxy = new Ext.data.HttpProxy({
                   url: 'request?class=DatabaseInfo&method=getColumns&id='+connectionCombo.getValue()+'&catalogName='+databaseCombo.getValue()+'&tableName='+tableCombo.getValue(),
                   method : "POST"
                });

            var columnSelectorDataStore = new Ext.data.Store({
                proxy: columnSelectorProxy,
                reader: columnSelectorReader
            });
            
            columnSelectorDataStore.on('load', loadColumnsSuccessful);
            function loadColumnsSuccessful() {
                var rta = [];
                for (i = 0; i < columnSelectorDataStore.getCount(); i++) {
                        var rta2 = [];
                        rta2.push(columnSelectorDataStore.getAt(i).get('cname'));
                        rta.push(rta2);
                }
                Ext.getCmp("columnSelector").fromStore.loadData(rta,false);
                Ext.getCmp("columnSelector").toStore.removeAll();
            }

            columnSelectorDataStore.load();

        });


        var columnSelectorDef = Ext.data.Record.create( [ {
            name :'cname',
            type :'string'
        } ]);

        var columnStore = new Ext.data.ArrayStore({
            data: [[]],
            fields: columnSelectorDef,
            sortInfo: {
                field: 'cname',
                direction: 'ASC'
            }
        });

        var columnSelector = new Ext.ux.ItemSelector( {
            name :"columnSelector",
            id : "columnSelector",
            fieldLabel : "Columns",
            dataFields : ['cname'],
            toData : [[]],
            msWidth :150,
            msHeight :350,
            valueField :"cname",
            displayField :"cname",
            imagePath: 'icons/images/',
            toLegend :"Selected",
            fromLegend :"Available",
            fromStore :columnStore
    });
        

        var fileUploadPanel2 = new Ext.FormPanel({
            fileUpload: true,
            width: 500,
            hidden: true,
            title: 'Import Step 2',
            bodyStyle: 'padding: 10px 10px 0 10px;',
            labelWidth: 150,
            items: [connectionCombo,databaseCombo,tableCombo,columnSelector],
            buttons: [{
                text: 'Finish',
                handler: function(){
                    if(fileUploadPanel2.getForm().isValid()){
                            fileUploadPanel2.getForm().submit({
                                url: 'do?action=import',
                                waitMsg: 'Importing...',
                                success: function(result, request){
                                   
                                    alert("Success"+csvseparator+fileencoding);
                                }
                            });
                    }
                }
            },{
                text: 'Reset',
                handler: function(){
                    fileUploadPanel2.getForm().reset();
                }
            }]
        });

        importpage.add(fileUploadPanel1);
        importpage.add(fileUploadPanel2);
	tabFolder.add(importpage);
	tabFolder.setActiveTab(importpage);
}

function createExportTablePage(node){
	createTabPage("do?action=exportTablePage&id="+node.id,"icons/cd_edit.png","Export Table "+node.attributes.qname);
}

function createdriversPage(){
	createTabPage("do?action=driversPage","icons/application.png","drivers");
}

function createViewQueriesPage(sid, sourceName){
	createTabPage("do?action=queriesPage&sid="+sid,"icons/script.png",sourceName);
}


function createTabPage(_url,_icon,_title){
	var tabFolder = Ext.getCmp('tabpanelpages');
	
	var page = new Ext.Panel({
		autoScroll :true,
		closable:true,
		title :"<img src='"+_icon+"' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;"+_title
	});
	tabFolder.add(page);
	tabFolder.setActiveTab(page);
	page.load({showLoadIndicator:false,nocache:true, url: _url+"&pageid="+page.id, scripts:true});
}
function createTableGrid(queryID, meta, myData) {
	rta = [];
	rta.push(new Ext.grid.RowNumberer());

	for (i = 0; i < meta.length; i++) {
		rta.push( {
			header :defaultRenderer(meta[i]["l"]),
			dataIndex :meta[i]["l"],
			sortable :true,
			renderer :defaultRenderer,
			align:meta[i]["al"]
		});
	}
	var fieldArray = [];
	for (i = 0; i < meta.length; i++) {
		fieldArray.push( {
			name :meta[i]["l"]
		});
	}
	var dsStore = new Ext.data.SimpleStore( {
		fields :fieldArray
	});
	dsStore.loaded = false;

	return new Ext.grid.GridPanel( {
		border :false,
		queryID :queryID,
		store :dsStore,
		myData :myData,
		cm :new Ext.grid.ColumnModel(rta),
		stripeRows :true,
		view: new Ext.ux.grid.BufferView({
		    scrollDelay: false
	    }),
		listeners : {
			render : function() {
				this.store.loadData(this.myData);
				this.store.loaded = true;
			}
		},
		additionalData : function(addedData, totalData) {
			if (this.store.loaded) {
				this.store.loadData(addedData, true);
			}
		},
		refreshData: function(newData){
			if (this.store.loaded) {
				this.store.loadData(newData, false);
			}
		}
	});

};// end createTableGrid
function onLayoutCheck(item, checked) {
	if (checked) {
		preferredLayout = item.sel;
		item.clLayout.setActiveItem(item.activePanel);
		item.activePanel.fireEvent('render', item.activePanel);

	}
}// end onLayoutCheck

function sqlAdditionalSuccessful(response, options) {
	var object = Ext.util.JSON.decode(response.responseText);
	if (object.success) {
		options.sqlresultpanel.logPanel.info('Additional Data Retrieved.');
		var addedData = object.result.data;
		if (addedData.length > 0) {
			for ( var i = 0; i < addedData.length; i++) {
				options.myData.push(addedData[i]);
			}
			options.next.enable();
			for ( var i = 0; i < options.resultPanels.length; i++) {
				if (options.resultPanels[i].additionalData) {
					options.resultPanels[i].additionalData(addedData,
							options.myData);
				}
			}
		}else{
			options.next.disable();
		}
	} else {
		options.sqlresultpanel.logPanel.error(object.error);
		Ext.MessageBox.show( {
			title :'Error',
			msg :object.error,
			buttons :Ext.MessageBox.OK,
			icon :Ext.MessageBox.ERROR
		});
	}
}// sqlAdditionalSuccessful

function sqlAdditionalSuccessfulAll(response, options) {
	sqlAdditionalSuccessful(response,options);
	options.next.disable();
	options.nextAll.disable();
}
function RelViewer() {

	var relViewerToolbar = new Ext.Toolbar( {
		listeners : {
			render : function() {
								
				var clearViewer = new Ext.Toolbar.Button( {
					cls :'x-btn-icon',
					icon :'icons/page_white.png',
					tooltip :'<b>Clear</b><br/>Clear the Editor',
					// textareaid:textareaid,
					handler : function() {
						var workflow=this.relViewer.workflow;
						workflow.clear();
						workflow.addAddRemoveListener(workflow.riversqlListener);
					}
				});
				
				var gridToggle= new Ext.Toolbar.Button( {
					tooltip :'Show/Hide Grid',
					enableToggle :true,
					pressed :true,
					icon :'icons/grid.gif',
					cls :'x-btn-icon',
					handler : function() {
						//this.relViewer.workflow.html.style.backgroundImage="url(grid_10.png)";
						
						if (this.pressed == false){
							this.relViewer.workflow.html.style.backgroundImage="";
							this.relViewer.workflow.html.style.background='white';
						}else{
							this.relViewer.workflow.html.style.backgroundImage="url(grid_10.png)";
						}
					}
				});
				clearViewer.relViewer = this.relViewer;
				//open.relViewer = this.relViewer;

				//save.relViewer = this.relViewer;
				gridToggle.relViewer=this.relViewer;

                                var saveViewer = new Ext.Toolbar.Button( {
					cls :'x-btn-icon',
					icon :'icons/page_white.png',
					tooltip :'<b>Save</b><br/>Save the Graph',
					// textareaid:textareaid,
					handler : function() {
                                                alert(new draw2d.XMLSerializer().toXML(workflow.getDocument()));
					}
				});

				this.addButton(clearViewer);
				//this.addButton(open);
				//this.addButton(save);
				this.addButton(gridToggle);
                                this.addButton(saveViewer);
			}
		}
	});

	
	var scrollareaID=Ext.id();
	var scrollarea=document.createElement("div");
	scrollarea.style.overflow="auto";
	
	scrollarea.id=scrollareaID;
	
	var paintareaID=Ext.id();
	var paintarea=document.createElement("div");
	paintarea.id=paintareaID;
	paintarea.style.overflow="auto";
	paintarea.style.position="relative"
	paintarea.style.left="0px";
	paintarea.style.top="0px";
	paintarea.style.width="3000px";
	paintarea.style.height="3000px";
	scrollarea.appendChild(paintarea);
	
	var els=new Ext.Element(scrollarea);
	//els.region='center';
	var elp= new Ext.Element(paintarea);
	var canvas=new Ext.BoxComponent({el:els, region:'center'});
	
	var relViewer = new Ext.Panel( {
		
		title :"<img src='icons/table_relationship.png' style='vertical-align:bottom;height:16px;width:16px' />&nbsp;Tables Viewer",
		layout :'border',
		closable :true,
		//autoScroll :false,
		autoScroll:false,
		//id:Ext.id(),
		tbar :relViewerToolbar//,
		//bodyStyle:"overflow:hidden;overflow-x:auto;overflow-y:auto"
	});
	
	       	
	
	var outlineDataView = new Ext.DataView({
		emptyText :'No Figures yet',
		deferEmptyText:false,
		tpl :outlineTempl,
		singleSelect :true,
		store :new Ext.data.Store( {
       		proxy :new Ext.data.MemoryProxy([  ]),
       		reader :new Ext.data.ArrayReader( {}, [ {name :'tablefigure'}, {name :'title'} ])
		}),
		selectedClass :"ydataview-selected",
		itemSelector :'div.x-combo-list-item'
	});
	
	outlineDataView.on('selectionchange',function(outlineDataView,selectedElements){
		var r=outlineDataView.getSelectedRecords();
		if(r.length>0){
			var tableFigure=r[0].data.tablefigure;
			if(tableFigure!=null){
				var xpos=tableFigure.x;
				if(xpos>100){
					xpos=xpos-100;
				}
				var ypos=tableFigure.y;
				if(ypos>100){
					ypos=ypos-100;
				}
				outlineDataView.workflow.scrollTo(xpos,ypos,true);
				outlineDataView.workflow.setCurrentSelection(tableFigure);
				outlineDataView.workflow.showResizeHandles(tableFigure);
			}
		}
	});
	
	var outlinePanel = new Ext.Panel( {
		items :[outlineDataView],
		//id :'eastdivdrivers',
		deferredRender :false,
		width :200,
		split :true,
		collapsible :true,
		titleCollapse :true,
		layout :'fit',		
		title :'Outline',
		autoScroll :true,
		border :false,
		region:'east',
		outlineDataView:outlineDataView,
		onAdded:function(figure){
			if(figure instanceof draw2d.Line){}else{
				var data=[[figure,figure.title]];
				this.outlineDataView.store.loadData(data,true);
			}
			
		},
		onRemoved:function(figure){
			if(figure==null){
				this.outlineDataView.clearSelections(true);
				return;
			}
			if(figure instanceof draw2d.Line){}else{
				var idx=this.outlineDataView.store.find("title",figure.title);
				if(idx>-1){
					this.outlineDataView.store.remove(this.outlineDataView.store.getAt(idx));
				}
			}
		},
		onSelectionChanged:function(figure){
			if(figure==null){
				this.outlineDataView.clearSelections(true);
				return;
			}
			if(figure instanceof draw2d.Line){}else{
				var idx=this.outlineDataView.store.find("title",figure.title);
				if(idx>-1){
					var rc=this.outlineDataView.store.getAt(idx);
					this.outlineDataView.select(idx,false,true);
				}
			}
		}
	});

	
	relViewer.add(canvas);
	relViewer.add(outlinePanel);
	var tabFolder = Ext.getCmp('tabpanelpages');
	relViewerToolbar.relViewer = relViewer;

	tabFolder.add(relViewer);
	tabFolder.doLayout(false);
	tabFolder.activate(relViewer);
	//var workflow = new TableWorkflow(relViewer.body.id);
	var workflow = new TableWorkflow(paintareaID);
	
	var annotation = new draw2d.Annotation("");
	annotation.setStyledText("&nbsp;Drag and Drop tables from <b>Database View</b>...&nbsp;")
	annotation.setBackgroundColor(new draw2d.Color(255,128,64));
	annotation.setDimension(250,40);
	annotation.setDeleteable(false);
	annotation.setCanDrag(false);
	annotation.setResizeable(false);
	annotation.setSelectable(false);

	workflow.addFigure(annotation,10,10);
	
	workflow.scrollArea=scrollarea;
	outlineDataView.workflow=workflow;
	relViewer.workflow = workflow;
	workflow.riversqlListener=outlinePanel;
	workflow.addAddRemoveListener(workflow.riversqlListener);
	workflow.addSelectionListener(workflow.riversqlListener);
	
	var drop = new Ext.dd.DropTarget(
		relViewer.getEl(),
		{
			ddGroup :'testDD',
			_workflow :workflow,
			relViewer:relViewer,
			notifyDrop : function(dd, e, data) {
				var xOffset = this._workflow.getAbsoluteX();
				var yOffset = this._workflow.getAbsoluteY();
				var scrollLeft = this._workflow.getScrollLeft();
				var scrollTop = this._workflow.getScrollTop();

				var x = e.xy[0] - xOffset + scrollLeft;
				var y = e.xy[1] - yOffset + scrollTop;
				if (data.node.attributes["type"] == 'tb') {
					var tableFigure = new TableFigure(
							data.node.attributes["qname"]);
					workflow.addFigure(tableFigure, x, y);

					new Ext.data.Connection().request( {
						url :'do?action=getColumnsForViewer&id=' + data.node.attributes["id"],
						method :'post',
						scope :this,
						params : {
							tableFigure :tableFigure
						},
						callback : function(options, bSuccess,
								response) {
							var object = Ext
									.decode(response.responseText);
							arr = object.result.columns;
							var tableFigure = options.params.tableFigure;
							tableFigure.addColumns(arr);
						}
					});

					return true;
				}
				//else if (data.node.attributes["type"]=="tbs"){
//					reverseTables(this.relViewer,data.node.attributes["id"],workflow);
//					this.relViewer.getEl().mask("Reverse Engineering. Please, wait...", 'x-mask-loading');
//	                
//					new Ext.data.Connection().request( {
//						url :'do?action=reverseEngineering&id=' + data.node.attributes["id"],
//						method :'post',
//						scope :this,
//						callback : function(options, bSuccess,
//								response) {
//							var object = Ext
//									.decode(response.responseText);
//							this.relViewer.getEl().unmask();
//							var arr = object.result.tables;
//							for ( var i = 0; i < arr.length; i++) {
//								var tableFigure = new TableFigure(
//										arr[i].name);
//								workflow.addFigure(tableFigure, arr[i].x, arr[i].y);
//								tableFigure.addColumns(arr[i].columns)
//								tableFigure.collapse();
//							}
//							var connections=object.result.connections;
//							workflow.loadConnections(connections);
//							
//						}
//					//});
					//return true;
				//}
				return false;
			}
		}
	);
//	function reverseTables(relViewer,nodeid,workflow){
//		relViewer.getEl().mask("Reverse Engineering. Please, wait...", 'x-mask-loading');
//        
//		new Ext.data.Connection().request( {
//			url :'do?action=reverseEngineering&id=' + nodeid,
//			method :'post',
//			//scope :this,
//			relViewer:relViewer,
//			workflow:workflow,
//			callback : function(options, bSuccess,
//					response) {
//				var object = Ext
//						.decode(response.responseText);
//				relViewer.getEl().unmask();
//				var arr = object.result.tables;
//				for ( var i = 0; i < arr.length; i++) {
//					var tableFigure = new TableFigure(
//							arr[i].name);
//					workflow.addFigure(tableFigure, arr[i].x, arr[i].y);
//					tableFigure.addColumns(arr[i].columns)
//					tableFigure.collapse();
//				}
//				var connections=object.result.connections;
//				workflow.loadConnections(connections);
//				
//			}
//		});
//	}
	//workflow.scrollArea = document.getElementById(relViewer.id).parentNode;
//	relViewer.reverseTables=function(nodeid){
//		reverseTables(this,nodeid,this.workflow);
//	}
	return relViewer;
}// end RelViewer

function openConnectionDialog(alias) {

	var config = {
		width :500,
		height :200,
		shadow :true,
		minWidth :300,
		minHeight :150,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Connect to ' + alias.get('sourceName')
	};

	var dialogConnection = new Ext.Window(config);
	dialogConnection.addButton('Cancel', dialogConnection.close,
			dialogConnection);
	var dlgConnectionForm = new Ext.form.FormPanel( {
		labelWidth :95, // label settings here cascade unless overridden
		url :'do?action=connect',
		onSubmit :Ext.emptyFn,
		baseCls :'x-plain'
	});

	function onsubmit() {

		if (dlgConnectionForm.form.isValid()) {
			dlgConnectionForm.form.submit( {
				params : {
					driverid :alias.get('driverid'),
					sourceid :alias.get('id')
				},
				waitMsg :'Connecting...',
				failure :submitFailed,
				success :submitSuccessful
			});
		} else {
			Ext.MessageBox.show( {
				title :'Error',
				msg :'Please fix the errors noted.',
				buttons :Ext.MessageBox.OK,
				icon :Ext.MessageBox.ERROR
			});
		}

	}
	function submitSuccessful(form, action) {
		var pluginScripts=action.result.result.pluginScripts;
		if(pluginScripts){
			   for (var i = 0, l = pluginScripts.length; i < l; i++) {
				   load_plugin_script(pluginScripts[i][0]);
			   }
		}
		dialogConnection.close();
		databasesDataStore.reload();
	}
	dialogConnection.addButton('Connect', onsubmit, dialogConnection);

	dlgConnectionForm.add(new Ext.form.TextField( {
		fieldLabel :'Source',
		name :'source',
		width :300,
		readOnly :true,
		allowBlank :false,
		value :alias.get('sourceName')
	}),

	new Ext.form.TextField( {
		fieldLabel :'JDBC Url',
		name :'url',
		width :300,
		readOnly :true,
		value :alias.get('jdbcUrl')
	}),

	new Ext.form.TextField( {
		fieldLabel :'User',
		name :'user',
		width :300,
		value :alias.get('userName')
	}),

	new Ext.form.TextField( {
		inputType :'password',
		fieldLabel :'Password',
		name :'password',
		width :300
	}), new Ext.form.Checkbox( {
		fieldLabel :'Auto Commit',
		// boxLabel:'AutoCommit',
		name :'autocommit',
		checked :false,
		value :'1'
	}));

	dialogConnection.show();
	dlgConnectionForm.render(dialogConnection.body);
}// end openConnectionDialog

function nodeSelection(a, newnode, c) {

	if (synchro.pressed == false)
		return;
	if (newnode == c)
		return true;
	var tabKey = newnode.attributes.type + '-' + treecombo.getValue();
	if (dynTabsArray[tabKey] != null) {
		buildDynamicSouth(newnode, dynTabsArray[tabKey]);
	} else {
		new Ext.data.Connection().request( {
			url :'do?action=getDetails&nodeType=' + newnode.attributes.type
					+ '&sessionid=' + treecombo.getValue(),
			method :'post',
			scope :this,
			params :this.baseParams,
			success :detailsSuccessful,
			tabKey :tabKey,
			newnode :newnode
		});
	}
} // end nodeSelection

function buildDynamicSouth(newnode, arr) {

	southPanel.disable();

	var activeTab = southPanel.getActiveTab();

	var selectedIndex = -1
	if (activeTab) {
		selectedIndex = southPanel.items.indexOf(activeTab);
	}

	southPanel.beginUpdate();

	while (southPanel.items.getCount() > 0) {
		var last = southPanel.items.last();
		if (activeTab != last) {
			southPanel.remove(last);
		} else {
			if (southPanel.items.getCount() == 1) {
				southPanel.remove(last);
			} else {
				southPanel.remove(southPanel.items.first());
			}
		}
	}
	// southPanel.items.clear();
	southPanel.endUpdate();
	southPanel.doLayout(false);
	// southPanel.resumeEvents();

	var ilength = arr.length;
	var nextids = [];
	var cntPanel = [];

	for ( var i = 0; i < ilength; i++) {
		var title_ = arr[i][0];

		nextids[i] = Ext.id();

		var rta = [];
		var rtastore = [];

		rta.push( {
			header :"1",
			dataIndex :"1",
			sortable :false,
			width :90
		});
		rtastore.push( {
			name :"1"
		});

		var dummytmpstore = new Ext.data.SimpleStore( {
			fields :rtastore
		});
		dummytmpstore.loadData( [ [ "Loading..." ] ]);
		var dummyColumnModel = new Ext.grid.ColumnModel(rta);

		cntPanel[i] = new Ext.grid.GridPanel( {
			id :nextids[i],
			closable :false,
			border :false,
			layout :'fit',
			stripeRows :true,
			store :dummytmpstore,
			cm :dummyColumnModel
		});

		cntPanel[i].title = title_;
		cntPanel[i].url = arr[i][1];
		cntPanel[i].nextid = nextids[i];
		southPanel.add(cntPanel[i]);
		cntPanel[i].alreadyActivated = false;
		cntPanel[i].on('activate', function() {
			pnActivation(newnode, this);
		});

	}

	southPanel.enable();
	if (cntPanel != null && cntPanel.length > 0) {

		if (selectedIndex > -1) {
			if (selectedIndex < cntPanel.length) {
				southPanel.setActiveTab(cntPanel[selectedIndex]);
			} else {
				southPanel.setActiveTab(cntPanel[0]);
			}
		} else {
			southPanel.setActiveTab(cntPanel[0]);
		}

	}
} // end buildDynamicSouth

function pnActivation(newnode, panel) {
	if (panel.alreadyActivated == true)
		return;
	panel.alreadyActivated = true;
	var url_ = panel.url;
	var _nextid = panel.nextid;
	new Ext.data.Connection().request( {
		url :url_ + '&id=' + newnode.id,
		method :'post',
		scope :this,
		panelav :panel,
		nextid :_nextid,
		success :buildSouthTabItem
	});

} // end pnActivation

function buildSouthTabItem(response, options) {
	var av_id = options.nextid;
	var panelav = options.panelav;
	if (panelav) {

		var object = Ext.util.JSON.decode(response.responseText);
		if (object.success) {

			var rta = [];
			var rtastore = [];
			meta = object.result.meta;
			myData = object.result.data;

			rta.push(new Ext.grid.RowNumberer());
			for (i = 0; i < meta.length; i++) {
				// rta2.push({name: meta[i]});
				rta.push( {
					header :meta[i],
					dataIndex :meta[i],
					sortable :false,
					width :150
				});
				rtastore.push( {
					name :meta[i]
				});
			}
			var tmpstore = new Ext.data.SimpleStore( {
				fields :rtastore
			});

			panelav.reconfigure(tmpstore, new Ext.grid.ColumnModel(rta));
			panelav.store.loadData(myData);
		}
	}
	return true;
} // end buildSouthTabItem

function detailsSuccessful(response, options) {
	var object = Ext.decode(response.responseText);
	if (object.success) {
		details = object.result.details;
		dynTabsArray[options.tabKey] = details;
		buildDynamicSouth(options.newnode, details);
	}
}

function alterTable(node) {
	var droppedColums = [];
	var droppedIndexes = [];
	var droppedPK = [];
	var reader_types = new Ext.data.JsonReader( {
		root :'result.types',
		id :'tp'
	}, [ 'tp' ]);
	var type_store = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTypesAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :reader_types
	});
	type_store.load();

	var ColumnDef = Ext.data.Record.create( [ {
		name :'cname',
		type :'string'
	}, {
		name :'cnameold',
		type :'string'
	}, {
		name :'ctype',
		type :'string'
	}, {
		name :'csize',
		type :'int'
	}, {
		name :'cdecdig',
		type :'int'
	}, {
		name :'cacceptnull'
	}, {
		name :'ccom'
	}, {
		name :'cdefault'
	}, {
		name :'cnew',
		type :'boolean'
	}, {
		name :'cchanged',
		type :'boolean'
	}, {
		name :'cacceptnullold',
		type :'boolean'
	} ]);

	var IndexDef = Ext.data.Record.create( [ {
		name :'iname',
		type :'string'
	}, {
		name :'unique',
		type :'boolean'
	}, {
		name :'cols',
		type :'string'
	}, {
		name :'idxnew',
		type :'boolean'
	} ]);

	var PKDef = Ext.data.Record.create( [ {
		name :'pkname',
		type :'string'
	}, {
		name :'cols',
		type :'string'
	}, {
		name :'pknew',
		type :'boolean'
	} ]);

	var dsColumns = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTableColumnsAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.columns',
			id :'cname'
		}, ColumnDef)

	});

	var dsIndexes = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTableIndexesAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.indexes',
			id :'iname'
		}, IndexDef)
	});
	var dsPK = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getPKAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.pk',
			id :'pkname'
		}, PKDef)
	});

	var boolean_store = new Ext.data.SimpleStore( {
		fields : [ 'value' ],
		data : [ [ true ], [ false ] ]
	});

	var pkColModel = new Ext.grid.ColumnModel( [ {
		id :'pkname',
		header :'Primary Key Name',
		width :160,
		dataIndex :'pkname'
	}, {
		header :'Columns',
		width :120,
		dataIndex :'cols'
	} ]);
	pkColModel.defaultSortable = false;

	var indexesColModel = new Ext.grid.ColumnModel( [ {
		id :'iname',
		header :'Index Name',
		width :160,
		dataIndex :'iname'
	}, {
		header :'Unique',
		width :120,
		dataIndex :'unique'
	}, {
		header :'Columns',
		width :120,
		dataIndex :'cols'
	} ]);
	indexesColModel.defaultSortable = false;

	var colModel = new Ext.grid.ColumnModel( [ {
		id :'cname',
		header :"Column Name",
		width :160,
		dataIndex :'cname',

		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {
			allowBlank :false
		}))
	}, {
		header :"Type",
		width :75,
		dataIndex :'ctype',
		editor :new Ext.grid.GridEditor(new Ext.form.ComboBox( {
			typeAhead :true,
			triggerAction :'all',
			store :type_store,
			lazyRender :true,
			displayField :'tp',
			mode :'local',
			editable :false,
			resizable :true,
			listWidth :200
		}))
	}, {
		header :"Size",
		width :75,
		dataIndex :'csize',
		align :'right',
		editor :new Ext.grid.GridEditor(new Ext.form.NumberField( {
			allowDecimals :false,
			allowNegative :false,
			allowBlank :true
		}))
	}, {
		header :"Decimal Digits",
		width :75,
		dataIndex :'cdecdig',
		align :'right',
		editor :new Ext.grid.GridEditor(new Ext.form.NumberField( {
			allowDecimals :false,
			allowNegative :false,
			allowBlank :true
		}))
	}, {
		header :"Accept Null Values",
		width :100,
		dataIndex :'cacceptnull',
		editor :new Ext.grid.GridEditor(new Ext.form.ComboBox( {
			typeAhead :true,
			triggerAction :'all',
			store :boolean_store,
			lazyRender :true,
			displayField :'value',
			mode :'local',
			editable :false
		}))
	}, {
		header :"Remarks",
		width :75,
		dataIndex :'ccom',
		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {

		}))
	}, {
		header :"Default",
		width :75,
		dataIndex :'cdefault',
		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {

		}))
	} ]);

	colModel.defaultSortable = false;

	var addColumn = new Ext.Toolbar.Button( {
		text :'Add Column',
		cls :'x-btn-text-icon',
		icon :'icons/table_add.png',

		handler : function() {
			var p = new ColumnDef( {
				cname :'Column' + dsColumns.getCount(),
				ctype :'VARCHAR',
				csize :12,
				cdecdig :0,
				cacceptnull :true,
				ccom :'',
				cdefault :'',
				cnew :true
			});
			columnsgrid.stopEditing();
			dsColumns.insert(dsColumns.getCount(), p);
			columnsgrid.startEditing(dsColumns.getCount() - 1, 0);
		}
	});

	var dropColumn = new Ext.Toolbar.Button( {
		text :'Drop Column',
		cls :'x-btn-text-icon',
		icon :'icons/table_delete.png',
		handler : function() {
			columnsgrid.stopEditing();
			if (columnsgrid.getSelectionModel().hasSelection()) {
				var rc = columnsgrid.getSelectionModel().selection.record;
				droppedColums[droppedColums.length] = rc.get('cnameold');
				dsColumns.remove(rc);
			}
		}
	});

	var columnsgrid = new Ext.grid.EditorGridPanel( {
		ds :dsColumns,
		cm :colModel,
		trackMouseOver :true,
		title :'Columns',
		tbar : [ addColumn, new Ext.Toolbar.Separator(), dropColumn ]
	});

	columnsgrid.getSelectionModel().on('selectionchange', function() {
		if (columnsgrid.getSelectionModel().hasSelection()) {
			dropColumn.enable();
		} else {
			dropColumn.disable();
		}
	});
	dropColumn.disable();

	var addIndex = new Ext.Toolbar.Button( {
		cls :'x-btn-text-icon',
		icon :'icons/key_add.png',
		text :'Add Index',
		handler : function() {
			newIndexForAlterTable(node, IndexDef, dsIndexes, dsColumns);
		}
	});

	var dropIndex = new Ext.Toolbar.Button( {
		text :'Drop Index',
		cls :'x-btn-text-icon',
		icon :'icons/key_delete.png',
		handler : function() {
			if (indexesgrid.getSelectionModel().hasSelection()) {
				var rc = indexesgrid.getSelectionModel().getSelected();
				if (rc.get('idxnew') != true)
					droppedIndexes[droppedIndexes.length] = rc.get('iname');
				dsIndexes.remove(rc);
			}
		}
	});

	var indexesgrid = new Ext.grid.EditorGridPanel( {// cpIndexes.getId(), {
				ds :dsIndexes,
				cm :indexesColModel,
				// fitToFrame: true,
				trackMouseOver :true,
				title :'Indexes',
				selModel :new Ext.grid.RowSelectionModel( {
					singleSelect :true
				}),
				tbar : [ addIndex, new Ext.Toolbar.Separator(), dropIndex ]
			});

	indexesgrid.getSelectionModel().on('selectionchange', function() {
		if (indexesgrid.getSelectionModel().hasSelection()) {
			dropIndex.enable();
		} else {
			dropIndex.disable();
		}
	});
	dropIndex.disable();

	var addPK = new Ext.Toolbar.Button( {
		cls :'x-btn-text-icon',
		icon :'icons/table_key.png',
		text :'Add PK',
		handler : function() {
			newPKForAlterTable(node, PKDef, dsPK, dsColumns);
		}
	});

	var dropPK = new Ext.Toolbar.Button( {
		text :'Drop PK',
		cls :'x-btn-text-icon',
		icon :'icons/key_delete.png',
		handler : function() {
			var rcPK = dsPK.getAt(0);
			if (rcPK.get('pknew') != true)
				droppedPK[0] = rcPK.get('pkname');
			dsPK.remove(rcPK);
		}
	});

	var pkgrid = new Ext.grid.GridPanel( {// cpPK.getId(), {
				ds :dsPK,
				cm :pkColModel,
				border :false,
				title :'Primary Key',
				trackMouseOver :true,
				tbar : [ addPK, new Ext.Toolbar.Separator(), dropPK ]
			});

	var dialogTabPanel = new Ext.TabPanel( {

		autoTabs :true,
		activeTab :0,
		deferredRender :false,
		border :false
	});

	dialogTabPanel.add(columnsgrid);
	dialogTabPanel.add(indexesgrid);
	dialogTabPanel.add(pkgrid);
	dialog = new Ext.Window( {
		layout :'fit',
		width :650,
		height :500,
		modal :true,
		items :dialogTabPanel,
		title :'Alter Table ' + node.text
	});
	function onClose() {
		columnsgrid.stopEditing();
		dialog.close();
	}

	dialog.addButton('Close', onClose, dialog);

	function onAlterTableSubmit() {
		var count = dsColumns.getCount();
		var newColums = [];
		var changedColums = [];
		var newIndexes = [];
		var newPK = [];
		var countIdx = dsIndexes.getCount();
		for ( var i = 0; i < countIdx; i++) {
			var rcIdx = dsIndexes.getAt(i);
			if (rcIdx.get('idxnew') == true) {
				var obj = new Object();
				obj['iname'] = rcIdx.get('iname');
				obj['cols'] = rcIdx.get('cols');
				obj['unique'] = rcIdx.get('unique');
				newIndexes[newIndexes.length] = obj;
			}
		}
		var countPK = dsPK.getCount();
		for ( var i = 0; i < countPK; i++) {
			var rcPK = dsPK.getAt(i);
			if (rcPK.get('pknew') == true) {
				var obj = new Object();
				obj['pkname'] = rcPK.get('pkname');
				obj['cols'] = rcPK.get('cols');
				newPK[0] = obj;
			}
		}
		for ( var i = 0; i < count; i++) {
			var rc = dsColumns.getAt(i);
			if (rc.get('cnew') == true) {
				var obj = new Object();

				obj['cname'] = rc.get('cname');
				obj['ctype'] = rc.get('ctype');
				obj['csize'] = rc.get('csize');
				obj['cdecdig'] = rc.get('cdecdig');
				obj['cacceptnull'] = rc.get('cacceptnull');
				obj['ccom'] = rc.get('ccom');
				obj['cdefault'] = rc.get('cdefault');

				newColums[newColums.length] = obj;
			} else if (rc.get('cchanged') == true) {
				var obj = new Object();
				obj['cname'] = rc.get('cname');
				obj['cnameold'] = rc.get('cnameold');
				obj['ctype'] = rc.get('ctype');
				obj['csize'] = rc.get('csize');
				obj['cdecdig'] = rc.get('cdecdig');
				obj['cacceptnull'] = rc.get('cacceptnull');
				obj['ccom'] = rc.get('ccom');
				obj['cdefault'] = rc.get('cdefault');
				obj['cacceptnullold'] = rc.get('cacceptnullold');
				changedColums[changedColums.length] = obj;
			}
		}

		new Ext.data.Connection().request( {
			url :'do?action=generateDDLAlterTable',
			success : function(response) {
				var rt = Ext.decode(response.responseText);
				dialog.close();
				newEditor(rt.result.ddl);

			},
			failure : function() {
				Ext.MessageBox.alert('Failure', "");
				dialog.close();
			},
			method :'POST',
			params : {
				tableid :node.id,
				newCols :Ext.encode(newColums),
				changedCols :Ext.encode(changedColums),
				droppedCols :Ext.encode(droppedColums),
				droppedIdx :Ext.encode(droppedIndexes),
				newIdxs :Ext.encode(newIndexes),
				newPKs :Ext.encode(newPK),
				droppedPKs :Ext.encode(droppedPK)
			}
		});

	}

	dialog.addButton('Generate DDL to Alter Table', onAlterTableSubmit, dialog);

	dialog.show();

	dialogTabPanel.on('tabchange', function() {
		columnsgrid.stopEditing();
	});

	columnsgrid.on('afteredit', function(evt) {
		if (evt.value != evt.originalValue) {
			if (evt.record.get('cnew') != true) {
				evt.record.set('cchanged', true)
			}
		}
	});

	dsColumns.load();
	dsIndexes.load();

	dsPK.load();

	function dsPKChanged() {
		if (dsPK.getCount() == 0) {
			dropPK.disable();
			addPK.enable();
		} else {
			dropPK.enable();
			addPK.disable();

		}
	}
	dsPK.on('datachanged', function() {
		dsPKChanged();
	});
	dsPK.on('remove', function() {
		dsPKChanged();
	});
	dsPK.on('add', function() {
		dsPKChanged();
	});

}// end alterTable

function createTable(node) {
	var reader_types = new Ext.data.JsonReader( {
		root :'result.types',
		id :'tp'
	}, [ 'tp' ]);
	var type_store = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTypesAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :reader_types
	});
	type_store.load();

	var ColumnDef = Ext.data.Record.create( [ {
		name :'cname',
		type :'string'
	}, {
		name :'cnameold',
		type :'string'
	}, {
		name :'ctype',
		type :'string'
	}, {
		name :'csize',
		type :'int'
	}, {
		name :'cdecdig',
		type :'int'
	}, {
		name :'cacceptnull'
	}, {
		name :'ccom'
	}, {
		name :'cdefault'
	}, {
		name :'cnew',
		type :'boolean'
	} ]);

	var IndexDef = Ext.data.Record.create( [ {
		name :'iname',
		type :'string'
	}, {
		name :'unique',
		type :'boolean'
	}, {
		name :'cols',
		type :'string'
	}, {
		name :'idxnew',
		type :'boolean'
	} ]);

	var PKDef = Ext.data.Record.create( [ {
		name :'pkname',
		type :'string'
	}, {
		name :'cols',
		type :'string'
	}, {
		name :'pknew',
		type :'boolean'
	} ]);

        
        var dsColumns = new Ext.data.ArrayStore(ColumnDef);

        var dsIndexes = new Ext.data.ArrayStore(IndexDef);

        var dsPK = new Ext.data.ArrayStore(PKDef);

        /*
        var myData = [
        ['ColumnName','ColumnOld','INT',50,0,0,0,0,0,0,0],
        ['ColumnName','ColumnOld','INT',50,0,0,0,0,0,0,0]
        ];
        dsColumns.loadData(myData);
        */

        /*
	var dsColumns = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTableColumnsAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.columns',
			id :'cname'
		}, ColumnDef)

	});
        
	var dsIndexes = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getTableIndexesAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.indexes',
			id :'iname'
		}, IndexDef)
	});
	var dsPK = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :'do?action=getPKAlterTable&id=' + node.id,
			method :'post'
		}),
		reader :new Ext.data.JsonReader( {
			root :'result.pk',
			id :'pkname'
		}, PKDef)
	});
        */

	var boolean_store = new Ext.data.SimpleStore( {
		fields : [ 'value' ],
		data : [ [ true ], [ false ] ]
	});

	var pkColModel = new Ext.grid.ColumnModel( [ {
		id :'pkname',
		header :'Primary Key Name',
		width :160,
		dataIndex :'pkname'
	}, {
		header :'Columns',
		width :120,
		dataIndex :'cols'
	} ]);
	pkColModel.defaultSortable = false;

	var indexesColModel = new Ext.grid.ColumnModel( [ {
		id :'iname',
		header :'Index Name',
		width :160,
		dataIndex :'iname'
	}, {
		header :'Unique',
		width :120,
		dataIndex :'unique'
	}, {
		header :'Columns',
		width :120,
		dataIndex :'cols'
	} ]);
	indexesColModel.defaultSortable = false;

	var colModel = new Ext.grid.ColumnModel( [ {
		id :'cname',
		header :"Column Name",
		width :160,
		dataIndex :'cname',

		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {
			allowBlank :false
		}))
	}, {
		header :"Type",
		width :75,
		dataIndex :'ctype',
		editor :new Ext.grid.GridEditor(new Ext.form.ComboBox( {
			typeAhead :true,
			triggerAction :'all',
			store :type_store,
			lazyRender :true,
			displayField :'tp',
			mode :'local',
			editable :false,
			resizable :true,
			listWidth :200
		}))
	}, {
		header :"Size",
		width :75,
		dataIndex :'csize',
		align :'right',
		editor :new Ext.grid.GridEditor(new Ext.form.NumberField( {
			allowDecimals :false,
			allowNegative :false,
			allowBlank :true
		}))
	}, {
		header :"Decimal Digits",
		width :75,
		dataIndex :'cdecdig',
		align :'right',
		editor :new Ext.grid.GridEditor(new Ext.form.NumberField( {
			allowDecimals :false,
			allowNegative :false,
			allowBlank :true
		}))
	}, {
		header :"Accept Null Values",
		width :100,
		dataIndex :'cacceptnull',
		editor :new Ext.grid.GridEditor(new Ext.form.ComboBox( {
			typeAhead :true,
			triggerAction :'all',
			store :boolean_store,
			lazyRender :true,
			displayField :'value',
			mode :'local',
			editable :false
		}))
	}, {
		header :"Remarks",
		width :75,
		dataIndex :'ccom',
		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {

		}))
	}, {
		header :"Default",
		width :75,
		dataIndex :'cdefault',
		editor :new Ext.grid.GridEditor(new Ext.form.TextField( {

		}))
	} ]);

	colModel.defaultSortable = false;

	var addColumn = new Ext.Toolbar.Button( {
		text :'Add Column',
		cls :'x-btn-text-icon',
		icon :'icons/table_add.png',

		handler : function() {
			var p = new ColumnDef( {
				cname :'Column' + dsColumns.getCount(),
				ctype :'VARCHAR',
				csize :12,
				cdecdig :0,
				cacceptnull :true,
				ccom :'',
				cdefault :'',
				cnew :true
			});
			columnsgrid.stopEditing();
			dsColumns.insert(dsColumns.getCount(), p);
			columnsgrid.startEditing(dsColumns.getCount() - 1, 0);
		}
	});

	var dropColumn = new Ext.Toolbar.Button( {
		text :'Drop Column',
		cls :'x-btn-text-icon',
		icon :'icons/table_delete.png',
		handler : function() {
			columnsgrid.stopEditing();
			if (columnsgrid.getSelectionModel().hasSelection()) {
				var rc = columnsgrid.getSelectionModel().selection.record;
				dsColumns.remove(rc);
			}
		}
	});

	var columnsgrid = new Ext.grid.EditorGridPanel( {
		ds :dsColumns,
		cm :colModel,
		trackMouseOver :true,
		title :'Columns',
		tbar : [ addColumn, new Ext.Toolbar.Separator(), dropColumn ]
	});

	columnsgrid.getSelectionModel().on('selectionchange', function() {
		if (columnsgrid.getSelectionModel().hasSelection()) {
			dropColumn.enable();
		} else {
			dropColumn.disable();
		}
	});
	dropColumn.disable();

	var addIndex = new Ext.Toolbar.Button( {
		cls :'x-btn-text-icon',
		icon :'icons/key_add.png',
		text :'Add Index',
		handler : function() {
			newIndexForAlterTable(node, IndexDef, dsIndexes,dsColumns);
		}
	});

	var dropIndex = new Ext.Toolbar.Button( {
		text :'Drop Index',
		cls :'x-btn-text-icon',
		icon :'icons/key_delete.png',
		handler : function() {
			if (indexesgrid.getSelectionModel().hasSelection()) {
				var rc = indexesgrid.getSelectionModel().getSelected();
				dsIndexes.remove(rc);
			}
		}
	});

	var indexesgrid = new Ext.grid.EditorGridPanel( {// cpIndexes.getId(), {
				ds :dsIndexes,
				cm :indexesColModel,
				// fitToFrame: true,
				trackMouseOver :true,
				title :'Indexes',
				selModel :new Ext.grid.RowSelectionModel( {
					singleSelect :true
				}),
				tbar : [ addIndex, new Ext.Toolbar.Separator(), dropIndex ]
			});

	indexesgrid.getSelectionModel().on('selectionchange', function() {
		if (indexesgrid.getSelectionModel().hasSelection()) {
			dropIndex.enable();
		} else {
			dropIndex.disable();
		}
	});
	dropIndex.disable();

	var addPK = new Ext.Toolbar.Button( {
		cls :'x-btn-text-icon',
		icon :'icons/table_key.png',
		text :'Add PK',
		handler : function() {
			newPKForAlterTable(node, PKDef, dsPK, dsColumns);
		}
	});

	var dropPK = new Ext.Toolbar.Button( {
		text :'Drop PK',
		cls :'x-btn-text-icon',
		icon :'icons/key_delete.png',
		handler : function() {
			var rcPK = dsPK.getAt(0);
			dsPK.remove(rcPK);
		}
	});

	var pkgrid = new Ext.grid.GridPanel( {// cpPK.getId(), {
				ds :dsPK,
				cm :pkColModel,
				border :false,
				title :'Primary Key',
				trackMouseOver :true,
				tbar : [ addPK, new Ext.Toolbar.Separator(), dropPK ]
			});

	var dialogTabPanel = new Ext.TabPanel( {
		height :400,
                autoTabs :true,
		activeTab :0,
		deferredRender :false,
		border :false
	});

	dialogTabPanel.add(columnsgrid);
	dialogTabPanel.add(indexesgrid);
	dialogTabPanel.add(pkgrid);

        var newDBForm = new Ext.form.FormPanel( {
		labelWidth :95,
		onSubmit :Ext.emptyFn,
		baseCls :'x-plain'
	});
        
        var newDBName = new Ext.form.TextField( {
            fieldLabel :'Table Name',
            name :'dbname',
            width :180,
            readOnly :false,
            allowBlank :false
	});

        newDBForm.add(newDBName);
        newDBForm.add(dialogTabPanel);

	dialog = new Ext.Window( {
		layout :'fit',
		width :650,
		height :500,
		modal :true,
		items :[newDBForm],
		title :'Create Table at Schema: ' + node.text

	});
	function onClose() {
		columnsgrid.stopEditing();
		dialog.close();
	}

	dialog.addButton('Close', onClose, dialog);

	function onCreateTableSubmit() {
		var newColums = [];
		var newIndexes = [];
		var newPK = [];
		var countIdx = dsIndexes.getCount();

		for ( var i = 0; i < countIdx; i++) {
			var rcIdx = dsIndexes.getAt(i);
			if (rcIdx.get('idxnew') == true) {
				var obj = new Object();
				obj['iname'] = rcIdx.get('iname');
				obj['cols'] = rcIdx.get('cols');
				obj['unique'] = rcIdx.get('unique');
				newIndexes[newIndexes.length] = obj;
			}
		}
		var countPK = dsPK.getCount();
		for ( var i = 0; i < countPK; i++) {
			var rcPK = dsPK.getAt(i);
			if (rcPK.get('pknew') == true) {
				var obj = new Object();
				obj['pkname'] = rcPK.get('pkname');
				obj['cols'] = rcPK.get('cols');
				newPK[newPK.length] = obj;
			}
		}
		var count = dsColumns.getCount();
		for ( var i = 0; i < count; i++) {
			var rc = dsColumns.getAt(i);
			if (rc.get('cnew') == true) {
				var obj = new Object();

				obj['cname'] = rc.get('cname');
				obj['ctype'] = rc.get('ctype');
				obj['csize'] = rc.get('csize');
				obj['cdecdig'] = rc.get('cdecdig');
				obj['cacceptnull'] = rc.get('cacceptnull');
				obj['ccom'] = rc.get('ccom');
				obj['cdefault'] = rc.get('cdefault');

				newColums[newColums.length] = obj;
			} 
		}

		new Ext.data.Connection().request( {
			url :'do?action=generateDDLCreateTable',
			success : function(response) {
				var rt = Ext.decode(response.responseText);
				dialog.close();
				newEditor(rt.result.ddl);

			},
			failure : function() {
				Ext.MessageBox.alert('Failure', "");
				dialog.close();
			},
			method :'POST',
			params : {
				tableid :node.id,
                                schemaname :node.text,
                                tablename :newDBName.getValue(),
				newCols :Ext.encode(newColums),
				newIdxs :Ext.encode(newIndexes),
				newPKs :Ext.encode(newPK)
			}
		});

	}

	dialog.addButton('Generate DDL to Create Table', onCreateTableSubmit, dialog);

	dialog.show();

	dialogTabPanel.on('tabchange', function() {
		columnsgrid.stopEditing();
	});

	//dsColumns.load();
	//dsIndexes.load();

	//dsPK.load();

	function dsPKChanged() {
		if (dsPK.getCount() == 0) {
			dropPK.disable();
			addPK.enable();
		} else {
			dropPK.enable();
			addPK.disable();

		}
	}
	dsPK.on('datachanged', function() {
		dsPKChanged();
	});
	dsPK.on('remove', function() {
		dsPKChanged();
	});
	dsPK.on('add', function() {
		dsPKChanged();
	});

}
// end createTable

function newIndexForAlterTable(node, IndexDef, dsIndexes,dsColumns) {

	var config = {
		width :700,
		height :350,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Create Index for Table ' + node.text + "...",
		labelWidth :100
	};

	var dialog = new Ext.Window(config);

	dialog.addButton('Close', dialog.close, dialog);

	var uniqueCheckBox = new Ext.form.Checkbox( {
		boxLabel :'',
		name :'unique',
		// width:'auto',
		fieldLabel :'Unique',
		labelAlign :'left',
		value :'1'
	});
	var indexName = new Ext.form.TextField( {
		fieldLabel :'Index Name',
		name :'name',
		width :200,
		readOnly :false,
		allowBlank :false

	});

	columnSelector = new Ext.ux.ItemSelector( {
		name :"itemselector",
		fieldLabel :"Column",
		dataFields : [ "cname" ],
		toData : [ [] ],
		msWidth :250,
		msHeight :200,
		valueField :"cname",
		displayField :"cname",
		imagePath :"icons/images/",
		toLegend :"Selected",
		fromLegend :"Available",
		fromStore :dsColumns
	});

	var newIndexForm = new Ext.form.FormPanel( {
		labelAlign :'left',
		frame :true,
		url :'do?action=generateDDLCreateIndex',
		bodyStyle :'padding:5px 5px 0',
		items : [ indexName, uniqueCheckBox, columnSelector ]
	});

	function onNewIndexSubmit() {

		var values = [];
		for ( var i = 0; i < columnSelector.toStore.getCount(); i++) {
			record = columnSelector.toStore.getAt(i);
			var val = record.get(columnSelector.valueField);
			if (val != "")// columnSelector has a blank value in toStore
				values.push(val);
		}

		var p = new IndexDef( {
			iname :indexName.getValue(),
			cols :values,
			unique :uniqueCheckBox.getValue(),
			idxnew :true
		});

		dsIndexes.insert(dsIndexes.getCount(), p);
		dialog.close();
	}

	dialog.addButton('Ok', onNewIndexSubmit, dialog);
	dialog.show();
	newIndexForm.render(dialog.body);
} // end newIndexForAlterTable

function newPKForAlterTable(node, PKDef, dsPK, dsColumns) {
	var config = {
		width :700,
		height :350,
		shadow :true,
		minWidth :300,
		minHeight :250,
		modal :true,
		collapsible :false,
		closable :true,
		title :'Create Primary Key for Table ' + node.text + "...",
		labelWidth :100
	};

	var pkName = new Ext.form.TextField( {
		fieldLabel :'Primay Key Name',
		name :'name',
		width :200,
		readOnly :false,
		allowBlank :false

	});
	var dialog = new Ext.Window(config);

	dialog.addButton('Close', dialog.close, dialog);

	columnSelector = new Ext.ux.ItemSelector( {
		name :"itemselector",
		fieldLabel :"Column",
		dataFields : [ "cname" ],
		toData : [ [] ],
		msWidth :250,
		msHeight :200,
		valueField :"cname",
		displayField :"cname",
		imagePath :"icons/images/",
		toLegend :"Selected",
		fromLegend :"Available",
		fromStore :dsColumns
	});

	var newPKForm = new Ext.form.FormPanel( {
		labelAlign :'left',
		frame :true,
		bodyStyle :'padding:5px 5px 0',
		items : [ pkName, columnSelector ]
	});

	function onNewPKSubmit() {

		var values = [];
		for ( var i = 0; i < columnSelector.toStore.getCount(); i++) {
			record = columnSelector.toStore.getAt(i);
			var val = record.get(columnSelector.valueField);
			if (val != "")// columnSelector has a blank value in toStore
				values.push(val);
		}

		var p = new PKDef( {
			cols :values,
			pkname :pkName.getValue(),
			pknew :true
		});

		dsPK.insert(dsPK.getCount(), p);
		dialog.close();
	}

	dialog.addButton('Ok', onNewPKSubmit, dialog);
	dialog.show();
	newPKForm.render(dialog.body);
}// end newPKForAlterTable

function load_plugin_script(url_script){
	if (loaded_plugin_scripts[url_script])
		return;	
	//alert("load: "+url);
	try{
		var script= document.createElement("script");
		script.type= "text/javascript";
		script.src= url_script;
		script.charset= "UTF-8";
		var head= document.getElementsByTagName("head");
		head[0].appendChild(script);
	}catch(e){
		document.write('<sc'+'ript language="javascript" type="text/javascript" src="' + url_script + '" charset="UTF-8"></sc'+'ript>');
	}
}
function exportTable(node){
	createExportTablePage(node);
}