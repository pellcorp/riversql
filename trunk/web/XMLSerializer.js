draw2d.XMLSerializer=function(){
};
draw2d.XMLSerializer.prototype.type="XMLSerializer";
draw2d.XMLSerializer.prototype.toXML=function(_5829){
var xml="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
xml=xml+"<form>\n";
var _582b=_5829.getFigures();
for(var i=0;i<_582b.getSize();i++){
var _582d=_582b.get(i);
xml=xml+"<"+_582d.type+" x=\""+_582d.getX()+"\" y=\""+_582d.getY()+"\" id=\""+_582d.getId()+"\">\n";
xml=xml+this.getPropertyXML(_582d,"   ");
if(_582d instanceof draw2d.CompartmentFigure){
xml=xml+this.getChildXML(_582d,"   ");
}
xml=xml+"</"+_582d.type+">\n";
}
xml=xml+"</form>\n";
return xml;
};
draw2d.XMLSerializer.prototype.getChildXML=function(_582e,_582f){
var xml="";
var _5831=_582e.getChildren();
for(var i=0;i<_5831.getSize();i++){
var _5833=_5831.get(i);
xml=xml+_582f+"<"+_5833.type+" x=\""+_5833.getX()+"\" y=\""+_5833.getY()+"\" id=\""+_5833.getId()+"\">\n";
xml=xml+this.getPropertyXML(_5833,"   "+_582f);
if(_5833 instanceof draw2d.CompartmentFigure){
xml=xml+this.getChildXML(_5833,"   "+_582f);
}
xml=xml+_582f+"</"+_5833.type+">\n";
}
return xml;
};
draw2d.XMLSerializer.prototype.getPropertyXML=function(_5834,_5835){
var xml="";
var _5837=_5834.getProperties();
for(key in _5837){
var value=_5837[key];
if(value!==null){
xml=xml+_5835+"<property name=\""+key+"\" value=\""+value+"\">\n";
}
}
return xml;
};
