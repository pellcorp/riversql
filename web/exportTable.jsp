

<div style="padding:5px">
	<h2 >Export Table ${qname}</h2><br/>
	Table contains ${rowCount} rows.
</div>
<div style="padding:15px">
<form action="do" target="_blank" method="post">
<input type="hidden" name="action" value="doExport" />
<input type="hidden" name="id" value="${param.id}"/>
<input type="hidden" name="rnd" value="${rnd}" />
<table style="margin:1em; padding:1em;border-collapse:collapse;">
<tr><td style="padding-right:1em;">Rows to be exported</td><td><input type="text" name="count" value="${rowCount}"/></td></tr>
<tr><td style="padding-right:1em;padding-left:2em">PDF</td><td><input type="radio" name="format" value="pdf" checked="checked"/> </td></tr>
<tr><td style="padding-right:1em;padding-left:2em">MS Excel</td><td><input type="radio" name="format" value="excel" /></td></tr>
<tr><td colspan="2" align="right" style="padding:20px"><input type="submit" value="Proceed to export...." onclick="this.disabled='true';submit();"/></td></tr>
</table>
</form>
</div>