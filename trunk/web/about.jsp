<div id="int${pageid}">
<div style="padding:5px">
	<h2 >RiverSQL <span style="font-weight:bold;color:black">[v. ${riversql_version}]</span> </h2><br/>
<div style="padding:15px">
<img src="icons/arrow_refresh.png" style="vertical-align:middle"/>&nbsp;
<a class="page" href="#"  onclick="var page=(Ext.get('int${pageid}')); if(page) page.load({showLoadIndicator:false,nocache:true,url: 'do?action=about&pageid=${pageid}', scripts:true});">Refresh</a>
</div>

<% 

		Runtime run=Runtime.getRuntime();
			long freeMemory=run.freeMemory();
			long maxMemory=run.maxMemory();
			long totalMemory=run.totalMemory();
			
	request.setAttribute("freeMemory",freeMemory);
	request.setAttribute("maxMemory",maxMemory);
	request.setAttribute("totalMemory",totalMemory);
	String serverInfo=application.getServerInfo();

%>
<table><tr><td>Free Memory</td><td>${freeMemory}</td></tr>
	<tr><td>Max Memory</td><td>${maxMemory}</td></tr>
<tr><td>Total Memory</td><td>${totalMemory}</td></tr>
</table><br/>
	<p>RiverSQL is running on: <%=serverInfo %><br/></p>
	<p style="color:red"><br/><b>RiverSQL is built on top of the following software:</b><br/></p>
	<table style="border:0">
	<tr><td><b>The user interface</b></td><td><a href="http://extjs.com/"><img alt="" style="vertical-align:middle;border:0"  src="images/extjs2.png"/></a> </td></tr>
	<tr><td><b>The icon set</b></td><td><a href="http://www.famfamfam.com/"><img alt="" style="vertical-align:middle;border:0"  src="images/famfamfam.png"/></a> </td></tr>
	<tr><td><b>Apache libraries</b></td><td><a href="http://commons.apache.org/collections/"><img alt="" style="vertical-align:middle;border:0"  src="images/apache.png"/></a></td></tr>
	<tr><td><b>Apache Log4je</b></td><td><a href="http://logging.apache.org/log4j/"><img alt="" style="vertical-align:middle;border:0"  src="images/log4j.jpg"/></a></td></tr>
	<tr><td><b>Oracle Toplink Essentials</b> </td><td><a href="http://www.oracle.com/technology/products/ias/toplink/jpa/index.html"><img alt="" style="vertical-align:middle;border:0"  src="images/oralogo_small.gif"/></a></td></tr>
	<tr><td><b>Open-jACOB Draw2D</b></td><td><a href="http://draw2d.org/draw2d/"><img alt="" style="vertical-align:middle;border:0" src="images/draw2d.png"/></a></td></tr>
	<tr><td><b>EditArea</b></td><td><a href="http://www.cdolivet.com/index.php?page=editArea"><img alt="" style="vertical-align:middle;border:0" src="images/cdolivet.net.png"/></a></td></tr>
	<tr><td><b>JSON</b></td><td><a href="http://www.json.org/java/index.html"><img alt="" style="vertical-align:middle;border:0" src="images/json160.gif"/></a></td></tr>
	</table>
</div>
</div>