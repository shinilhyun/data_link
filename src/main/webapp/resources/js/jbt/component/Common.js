/**
 * 
 */

function copyToClipboard(el) {
    var range = document.createRange() // create new range object
    range.selectNodeContents(el) // set range to encompass desired element text
    var selection = window.getSelection() // get Selection object from currently user selected text
    selection.removeAllRanges() // unselect any user selected text (if any)
    selection.addRange(range) // add range to Selection object to select it
//    var copysuccess // var to check whether execCommand successfully executed
//    try{
//        copysuccess = document.execCommand("copy") // run command to copy selected text to clipboard
//    } catch(e){
//        copysuccess = false
//    }
//    return copysuccess
}


String.prototype.isBlank = function() {
	return (!this || /^\s*$/.test(this));
};

String.prototype.isEmpty = function() {
	return (this.length === 0 || !this.trim());
};

Number.prototype.format = function(){
    if(this==0) return 0;
 
    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');
 
    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
 
    return n;
};

function objClone(obj) {
	if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

function get_goto(url, parm, target) {
	var f = document.createElement('form');
	var objs, value;
	for (var key in parm) {
		value = parm[key];
	    objs = document.createElement('input');
	    objs.setAttribute('type', 'hidden');
	    objs.setAttribute('name', key);
	    objs.setAttribute('value', value);
	    f.appendChild(objs);
	}
	objs = document.createElement('input');
    objs.setAttribute('type', 'hidden');
    f.appendChild(objs);
	
	if (target) {
		f.setAttribute('target', target);
	}
	f.setAttribute('method', 'get');
	f.setAttribute('action', url);
	document.body.appendChild(f);
	f.submit();
}

function replaceAll(str, searchStr, replaceStr) {
	return str.split(searchStr).join(replaceStr); 
}

function comma(num){
    var len, point, str; 
       
    num = num + ""; 
    point = num.length % 3 ;
    len = num.length; 
   
    str = num.substring(0, point); 
    while (point < len) { 
        if (str != "") str += ","; 
        str += num.substring(point, point + 3); 
        point += 3; 
    } 
     
    return str;
 
}