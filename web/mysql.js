
function mysql_editUser(id,tableName,node)
{
    var userInfo_reader = new Ext.data.JsonReader( {
            root :'result.userinfos'
            },
            [{
                name :'username',
                mapping :'username',
                type :'string'
            }, {
                name :'host',
                mapping :'host',
                type :'string'
            }, {
                name :'fullname',
                mapping :'fullname',
                type :'string'
            }, {
                name :'description',
                mapping :'description',
                type :'string'
            }, {
                name :'email',
                mapping :'email',
                type :'string'
            } ]);


    var dialogTabPanel = new Ext.TabPanel( {
            autoTabs :true,
            activeTab :0,
            deferredRender :false,
            border :false
    });

    var userInfo = new Ext.form.FormPanel( {
            title: 'User Information',
            bodyStyle: 'padding:10px;',
            labelWidth :95,
            onSubmit :Ext.emptyFn,
            baseCls :'x-plain',
            reader :userInfo_reader,
            items: [{
                xtype:'fieldset',
                title: 'Login Information',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                id: 'psdarea',
                items :[{
                        fieldLabel: 'User Name',
                        name: 'username',
                        allowBlank:false,
                        id: 'username'
                    },{
                        fieldLabel: 'Password',
                        name: 'pass',
                        id: 'pass'
                    },{
                        fieldLabel: 'Confirm Password',
                        name: 'pass-cfrm',
                        vtype: 'password',
                        initialPassField: 'pass'
                    }, {
                        fieldLabel: 'Host',
                        name: 'host',
                        id: 'host'
                    }
                ]
            },{
                xtype:'fieldset',
                title: 'Additional Information',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                id: 'userarea',
                items :[{
                        fieldLabel: 'Full Name',
                        name: 'fullname',
                        id: 'fullname'
                    },{
                        fieldLabel: 'Description',
                        name: 'description',
                        id: 'description'
                    },{
                        fieldLabel: 'Email',
                        name: 'email',
                        vtype:'email',
                        id: 'email'
                    }
                ]
            }]
    });

    var privilegeTableDef = Ext.data.Record.create( [ {
        name :'databasename',
        type :'string'
    }, {
        name :'privilegeavailable',
        type :'array'
    }, {
        name :'privilegeselected',
        type :'array'
    } ]);

    var dsPrivilegeTable = new Ext.data.Store( {
        proxy :new Ext.data.HttpProxy( {
            url : 'do?action=pluginAction&pluginName=MySQLPlugin&class=GetUserInfo&method=schemaprivileges&id=' + node.id,
            method :'post'
        }),
        reader :new Ext.data.JsonReader( {
            root :'result.schemaprivileges',
            id :'databasename'
        }, privilegeTableDef)
    });

    var privilegeTableColModel = new Ext.grid.ColumnModel( [ {
        id :'databasename',
        header :'Database Name',
        width :130,
        dataIndex :'databasename'
    }]);

    privilegeTableColModel.defaultSortable = false;

    var privilegeSelectorDef = Ext.data.Record.create( [ {
        name :'privilegename',
        type :'string'
    } ]);

    var ds = new Ext.data.ArrayStore({
        data: [[]],
        fields: privilegeSelectorDef,
        sortInfo: {
            field: 'privilegename',
            direction: 'ASC'
        }
    });

    var privilegeSelector = new Ext.ux.ItemSelector( {
            name :"privilegeSelector",
            id : "privilegeSelector",
            dataFields : ['privilegename'],
            toData : [[]],
            msWidth :150,
            msHeight :350,
            columnWidth: .66,
            valueField :"privilegename",
            displayField :"privilegename",
            imagePath: 'icons/images/',
            toLegend :"Selected",
            fromLegend :"Available",
            fromStore :ds
    });

    var grid = new Ext.grid.GridPanel({
        id :'databaseGrid',
        ds :dsPrivilegeTable,
	cm :privilegeTableColModel,

        columnWidth: .33,
        height: 350,
        width: 150,

        trackMouseOver :true,
        selModel :new Ext.grid.RowSelectionModel( {
            singleSelect :true,
            listeners: {
                rowselect: function(selModel, row, rec) {
                    //Ext.getCmp("privilegeSelector").toStore.addSorted(dataselected);
                    Ext.getCmp("privilegeSelector").toStore.loadData(rec.get("privilegeselected"));
                    Ext.getCmp("privilegeSelector").fromStore.loadData(rec.get("privilegeavailable"));
                }
            }
        })
    });

    var privilegeInfo = new Ext.form.FormPanel({
        title: 'User Privilege',
        bodyStyle: 'padding:10px;',
        hideLabels: true,
        layout:'column',
        items:[
            grid
            ,
            privilegeSelector
            ]
    });

    var resource_reader = new Ext.data.JsonReader( {
            root :'result.userlimits'
            },
            [{
                name :'max_questions',
                mapping :'max_questions',
                type :'int'
            }, {
                name :'max_updates',
                mapping :'max_updates',
                type :'int'
            }, {
                name :'max_connections',
                mapping :'max_connections',
                type :'int'
            }, {
                name :'max_user_connections',
                mapping :'max_user_connections',
                type :'int'
            } ]); //max_questions,max_updates,max_connections,max_user_connections

    var resourceInfo = new Ext.form.FormPanel( {
            title: 'Resource',
            bodyStyle: 'padding:10px;',
            labelWidth :95,
            onSubmit :Ext.emptyFn,
            baseCls :'x-plain',
            reader : resource_reader,

            items: [{
                xtype:'fieldset',
                title: 'Limiting User Resources',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                items :[{
                        fieldLabel: 'Max Questions',
                        name: 'max_questions'
                    },{
                        fieldLabel: 'Max Updates',
                        name: 'max_updates'
                    },{
                        fieldLabel: 'Max Connections',
                        name: 'max_connections'
                    }, {
                        fieldLabel: 'Max User Connections',
                        name: 'max_user_connections'
                    }
                ]
            }]
    });

    dialogTabPanel.add(userInfo);
    dialogTabPanel.add(privilegeInfo);
    dialogTabPanel.add(resourceInfo);

    var dialog = new Ext.Window( {
            layout :'fit',
            width :650,
            height :500,
            modal :true,
            items :dialogTabPanel,
            title :'Create User ' + node.text
    });
    function onClose() {
            dialog.close();
    }
    function onCreateUserSubmit()
    {
        dialog.close();
    }
    
    dialog.addButton('Close', onClose, dialog);
    dialog.addButton('Done', onCreateUserSubmit, dialog);

    dialog.show();

    userInfo.form.load({
        url : 'do?action=pluginAction&pluginName=MySQLPlugin&class=GetUserInfo&method=userInfo&id=' + node.id,
        method :'post',
        waitMsg : 'Loading User Info...',
        success : function() {

        },
        failure: function()
        {

        }
    });

    dsPrivilegeTable.load();

    resourceInfo.form.load({
        url : 'do?action=pluginAction&pluginName=MySQLPlugin&class=GetUserInfo&method=userlimits&id=' + node.id,
        method :'post',
        waitMsg : 'Loading User Limits...',
        success : function() {

        },
        failure: function()
        {

        }
    });
    
}

function mysql_createNewUser(id,tableName,node)
{
    mysql_editUser(id,tableName,node);
}

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
			height:120,
			shadow:true,
			minWidth:300,
			minHeight:80,
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