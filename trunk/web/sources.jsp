
<%@page import="java.util.List"%>
<%@page import="com.riversql.entities.Source"%>
<div id='int${pageid}'>
<div style="padding:5px">
	<h2 >Sources</h2><br/>
</div>
<div style="padding:15px">
<img src="icons/arrow_refresh.png" style="vertical-align:middle"/>&nbsp;
<a class="page" href="#"   onclick="var page=(Ext.get('int${pageid}')); if(page) page.load({showLoadIndicator:false,nocache:true,url: 'do?action=sourcesPage&pageid=${pageid}', scripts:true});">Refresh</a>
</div>

<script type="text/javascript">
 function testSourceConnection(sourceid){

	 Ext.Msg.prompt('Password', 'Please enter the password:', function(btn, text_){
		    if (btn == 'ok'){

		    	new Ext.data.Connection().request( {
		    		url :'do?action=testSourceConnection&id=' + sourceid,
		    		method :'post',
		    		scope :this,
		    		params :{password: text_},
		    		callback : function(options, bSuccess, response) {
		    			var object = Ext.util.JSON.decode(response.responseText);
		    			if (object.success) {
		    				Ext.DomHelper.overwrite("sp<%=request.getAttribute("rn") %>"+sourceid,"&nbsp;&nbsp;<img style='vertical-align:middle' src='icons/accept.png'/>");
		    			}else{
		    				Ext.DomHelper.overwrite("sp<%=request.getAttribute("rn") %>"+sourceid,"&nbsp;&nbsp;<img style='vertical-align:middle' src='icons/cross.png'/>");
		    				Ext.Msg.alert('Failed',object.error);
		    			}
		    		}
		    	});
		    }
		});
 }

</script>
<table style="margin:1em; padding:1em;border-collapse:collapse; ">
<% List<Source> ls=(List<Source>)request.getAttribute("ls");
   for(int i=0;i<ls.size();i++){ Source source=ls.get(i);%>
	<tr><td style="padding:5px;border:1px solid green;color:green;font-weight:bold;vertical-align:top"><%= source.getSourceName() %><span id="sp<%=request.getAttribute("rn") %><%= source.getId() %>"></span>
	<br/>
<br/><img src="icons/transmit_blue.png" style="vertical-align:middle"/>&nbsp;<a  class="page" href="#" onclick="testSourceConnection(<%= source.getId() %>)">Test Connection</a>
		</td>
		<td style="vertical-align:top;border:1px solid green;padding:5px"><%=source.getJdbcUrl() %><br/><br/><%=source.getUserName() %> </td>
	</tr>	   
   <% }
%>
</table>
</div>