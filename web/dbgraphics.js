
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