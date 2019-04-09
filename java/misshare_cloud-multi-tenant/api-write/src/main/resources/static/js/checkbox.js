function checkAll(obj, itemName) {
	for ( var i = 0; i < document.getElementsByName(itemName).length; i++) {
		if (obj.checked == true) {
			document.getElementsByName(itemName)[i].checked = true;
		} else {
			document.getElementsByName(itemName)[i].checked = false;
		}
	}
}

function cancelCheckAll(checkAllId, itemName) {
	if (!itemName) {
		document.getElementById(checkAllId).checked = false;
		return;
	}
	for ( var i = 0; i < document.getElementsByName(itemName).length; i++) {
		if (document.getElementsByName(itemName)[i].checked == false) {
			document.getElementById(checkAllId).checked = false;
			return;
		}
	}
	if (i >= document.getElementsByName(itemName).length) {
		document.getElementById(checkAllId).checked = true;
	}
}

function checkedValues(itemName) {
	var array = new Array();
	$(':checked[name="' + itemName + '"]').each(function() {
		array.push($(this).val());
	});
	return array.join();
}