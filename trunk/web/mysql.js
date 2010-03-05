

function mysql_emptyTable(id,tableName,node){
	Ext.Msg.confirm('Empty Table '+tableName,'Do you really want to TRUNCATE TABLE '+tableName+'?',function(btn){
		
		if(btn=='yes'){
			new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=emptyTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName},
	    		callback : function(options, bSuccess, response) {
	    		}
	    	});
		}
	});
}
function mysql_dropTable(id,tableName,node){
	Ext.Msg.confirm('Drop Table '+tableName,'Do you really want to DROP TABLE '+tableName+'?',function(btn){
		
		if(btn=='yes'){
			new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=dropTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName},
	    		callback : function(options, bSuccess, response) {
	    			refreshNode(node.parentNode);
	    		}
	    	});
		}
	});
}
function mysql_renameTable(id,tableName,node){
	Ext.Msg.prompt('Rename Table '+tableName, 'Please enter the new table name:', function(btn, text_){
	    if (btn == 'ok'){
	    	new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=renameTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName,newName:text_},
	    		callback : function(options, bSuccess, response) {
	    			refreshNode(node.parentNode);
	    		}
	    	});
	    	
	    }
	});
}

function mysql_createDatabase(id){
	var collationsReader = new Ext.data.JsonReader({
		root: 'result.collations',				 
		 	id: 'collation_name'							
		},['collation_name' ,'character_set_name']);
	
	var collationsProxy = new Ext.data.HttpProxy({
		   url: 'do?action=pluginAction&pluginName=MySQLPlugin&method=getCollations_cset&id='+id,
		   method : "POST"
		});
	
	var collationsDataStore = new Ext.data.Store({
	    proxy: collationsProxy,
	    reader: collationsReader
	});
	collationsDataStore.load();
	var xCollationsTempl=new Ext.XTemplate('<tpl for="."><div class="search-item">',
            '<span nowrap="nowrap">&nbsp;{character_set_name} <b>{collation_name}</b> </span>',
        '</div></tpl>'
       );
	
	var combo = new Ext.form.ComboBox({
		
			store:collationsDataStore,
	        typeAhead: true,
	        editable:false,
	        mode: 'local',
	        triggerAction: 'all',
	        selectOnFocus:true,
	        width:200,
	        forceSelection:true,
	        fieldLabel:'collation',
	        itemSelector: 'div.search-item',
	        tpl: xCollationsTempl,
	        displayField:'collation_name',
	    	valueField:'collation_name', 
	    	resizable:true

	});
	
	var config = {
			width:400,
			height:200,
			shadow:true,
			minWidth:300,
			minHeight:100,
			modal: true,
			collapsible: false,
			closable: true,
			title:'New Database...' 
		};
		
		var dialog = new Ext.Window(config);
		dialog.addButton('Cancel', dialog.close, dialog);
		var createDBForm =  new Ext.form.FormPanel({
			labelWidth: 95, 
			url:'do?action=pluginAction&pluginName=MySQLPlugin&method=createDB' ,
			onSubmit: Ext.emptyFn,
			baseCls: 'x-plain'             
		});
		dbname=new Ext.form.TextField({
	        fieldLabel: 'Database Name',
	        name: 'name',
	        width:200,
	        readOnly:false,
	        allowBlank:false
	    });
		createDBForm.add(
				dbname,combo
		);
		function onCreateDBSubmit(){
			if (createDBForm.form.isValid()) {
				createDBForm.form.submit(
				{
					params:{
						id:id ,
						collation:combo.getValue() 
					},
					waitMsg:'...',
					failure: submitFailed,			
					success: submitSuccessful
				}
				);
			}else{
				Ext.MessageBox.alert('Error Message', 'Please fix the errors noted.');
			}
		}
		dialog.addButton('Generate DDL', onCreateDBSubmit, dialog);
		
		combo.setValue('');
		dialog.show();
		createDBForm.render(dialog.body);
		function submitSuccessful(form, action) {
			dialog.close();
			var object=Ext.decode(action.response.responseText);
			
			var str=object.result.string;
			newEditor(str);
			
		}
}


loaded_plugin_scripts["mysql.js"] = true;