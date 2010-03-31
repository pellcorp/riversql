<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" >



<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

	<link rel="stylesheet" type="text/css" href="ext3.1.0/resources/css/riversql-min.css"/>
        <link rel="stylesheet" type="text/css" href="MultiSelect.css"/>
        <link rel="stylesheet" type="text/css" href="fileuploadfield.css"/>
	<link rel="shortcut icon" type="image/ico" href="favicon.ico"/>

    <title>RiverSQL</title>
    
	<style type="text/css">
	#loading-mask{
        position:absolute;
        left:0;
        top:0;
        width:100%;
        height:100%;
        z-index:20000;
        background-color:white;
    }
    #loading{
        position:absolute;
        left:45%;
        top:40%;
        padding:2px;
        z-index:20001;
        height:auto;
    }
    #loading a {
        color:#225588;
    }
    #loading .loading-indicator{
        background:white;
        color:#444;
        font:bold 13px tahoma,arial,helvetica;
        padding:10px;
        margin:0;
        height:auto;
    }
    #loading-msg {
        font: bold 10px arial,tahoma,sans-serif;color:red
    }
    
   body {
   		font-size:12px;font-family:verdana,geneva,lucida,'lucida grande',arial,helvetica,sans-serif;
        background-color:#fff;
        border:1px solid;
        border-color:#fafafa #fafafa #fafafa #fafafa;
    }
#home  A    {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
#home  A:link    {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline}   
#home A:visited { cursor:pointer;font-weight:bold;color: #3E62A6; text-decoration:underline}  
#home A:hover   { cursor:pointer;font-weight:bold;color: #CE8B10;text-decoration:underline }
#home A:active  { cursor:pointer;font-weight:bold; color: #3E62A6;text-decoration:underline }

.page {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
.page:link {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
.page:visited {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
.page:hover {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
.page:active {cursor:pointer;font-weight:bold; color: #3E62A6; text-decoration:underline} 
		
		.sqltable{
			margin:1em;
			border-collapse:collapse; 
			border-spacing:0
		}
		
		.sqlth{
			font-size:smaller;
			font-weight: bold; 
			background-color:  #e3e3e3;
			border-width: 0.1em;
			border-color:#000000;
			border-style:solid;
			padding:2px;
			font-family:verdana,geneva,lucida,'lucida grande',arial,helvetica,sans-serif;
		}
		
		.sqltd{
			padding:2px;
			border-width: 0.1em;
			border-color:#000000;
			border-style:solid
		}
		
		.x-selectable, .x-selectable * {
			-moz-user-select: text!important;
			-khtml-user-select: text!important;
		}
		
		
		.ydataview-selected{
			background-color: #c3daf9 !important;
			border: 1px solid #B5B8C8; overflow:auto;
			cursor:default;
		}
		.database  a span{color:#3E62A6;font-weight:bold; }
		.database .x-tree-node-icon{background:url('icons/database.png') no-repeat;}
		.schema {font-weight:bold;}
		.schema .x-tree-node-icon{background:url('icons/folder_database.png') no-repeat;}
		.table .x-tree-node-icon{background:url('icons/table.png') no-repeat;}
		.view .x-tree-node-icon{background:url('icons/application_view_tile.png') no-repeat;}
		.tables .x-tree-node-icon{background:url('icons/table_multiple.png') no-repeat;}
		.catalog {font-weight:bold;}
		.catalog .x-tree-node-icon{background:url('icons/folder_page.png') no-repeat;}
		.package .x-tree-node-icon{background:url('icons/package.png') no-repeat;}
		.packageb .x-tree-node-icon{background:url('icons/package_green.png') no-repeat;}
		.obj .x-tree-node-icon{background:url('icons/page_white_database.png') no-repeat;}
		.objs .x-tree-node-icon{background:url('icons/folder_page_white.png') no-repeat;}
		.views .x-tree-node-icon{background:url('icons/folder_page_white.png') no-repeat;}
		.trg .x-tree-node-icon{background:url('icons/database_gear.png') no-repeat;}
		
		.ux-mselect { border: 1px solid #B5B8C8; overflow:auto; cursor:default; }
		.ux-mselect-pointer { cursor:default; }
		.ux-mselect-valid { border: 1px solid #B5B8C8; overflow:auto; }
	    .ux-mselect-invalid { border:0; overflow:auto; }
	    
	    .x-log-entry { }
	.x-log-entry .x-log-level {
		float:left;
		width:4em;
		text-align:center;
		margin-right: 3px 
	}
	.x-log-entry .x-log-time {
		margin-right: 3px 
	}
	.x-log-entry .x-log-message {
		margin-right: 3px 
	}
	.x-log-debug { background-color: #46c }
	.x-log-info { background-color: lightgreen }
	.x-log-warning { background-color: yellow }
	.x-log-error { background-color: red }
	    
	#home div,p{
		font-size:12px;line-height: 200%;font-family:verdana,geneva,lucida,'lucida grande',arial,helvetica,sans-serif;
	}
	td{
		font-size:12px;line-height: 200%;font-family:verdana,geneva,lucida,'lucida grande',arial,helvetica,sans-serif;
	}
	</style>	
</head>
<body>

<div id="loading-mask" style=""></div>
<div id="loading">
    <div class="loading-indicator">Opening RiverSQL...<br/><span style="text-align:center" id="loading-msg"></span></div>
</div>


<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Loading Core API...';</script>
<script type="text/javascript" src="build/ext_all-min.js"></script>

<!--   <script type="text/javascript" src="grid_to_excel.js"></script> -->
<script type="text/javascript" src="build/draw_all.js"></script> 
 <script type="text/javascript" src="edit_area/edit_area_loader.js"></script> 
<script type="text/javascript">var sqlResultPluginFactories=[];</script>
<script type="text/javascript">document.getElementById('loading-msg').innerHTML = 'Building UI...';</script>
<script type="text/javascript" src="XMLSerializer.js"></script>
<script type="text/javascript" src="riversql.js"></script>
<script type="text/javascript" src="grid_to_excel.js"></script>
<script type="text/javascript" src="dbgraphics.js"></script>
<script type="text/javascript" src="gridFactories.js"></script>
<script type="text/javascript" src="mysql.js"></script>

<script type="text/javascript" src="FileUploadField.js"></script>

<!--<script type="text/javascript" src="build/riversql_all-min.js"></script>  -->
<!-- 

 <script type="text/javascript" src="dbgraphics.js"></script>
 <script type="text/javascript" src="gridFactories.js"></script>
 <script type="text/javascript" src="riversql.js"></script>-->

<script type="text/javascript">
//<![CDATA[	
	Ext.BLANK_IMAGE_URL = 'ext3.1.0/resources/images/default/s.gif';
	
	if (!Ext.grid.GridView.prototype.templates) {
		Ext.grid.GridView.prototype.templates = {};
	}
	Ext.grid.GridView.prototype.templates.cell = new Ext.Template(
		'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>',
		'<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>',
		'</td>'
	);
	Ext.grid.GridView.prototype.templates.cell.compile();
	Ext.onReady(function(){

		createPage();
	});
	
	
//]]> 
</script>

</body>
</html>