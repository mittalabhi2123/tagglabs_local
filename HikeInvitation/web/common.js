function showMessage(msg)
{
	var msgID = "loginForm:message";
	var oSpan = document.getElementById(msgID);
	if(oSpan)
	{
		oSpan.innerHTML = msg;
		return;
	}

	if(msg && msg.length > 0)
		alert(msg);
}

function showRequiredMessage(displayName)
{
	showMessage(displayName + " is a required.")
}

function setFocusElement(oElem)
{
	if(oElem && oElem.focus)
		oElem.focus();
	if(oElem && oElem.select)
		oElem.select();
}

function validateRequiredText(id, displayName)
{
	var oInput = document.getElementById(id);
	if(!oInput)
	{
		alert("Failed to find field: " + id);
		return false;
	}
	if(oInput.value.trim() == "")
	{
		showRequiredMessage(displayName);
		setFocusElement(oInput);
		return false;
	}
	return true;
}

function validateRequiredSelect(id, displayName)
{
	var oSelect = document.getElementById(id);
	if(!oSelect)
	{
		alert("Failed to find field: " + id);
		return false;
	}

	if(oSelect.selectedIndex < 1)	//will be -1 if no items or if bad selection from code
	{
		showRequiredMessage(displayName);
		setFocusElement(oSelect);
		return false;
	}
	return true;
}

function intOnly()
{
	var charCode = event.keyCode;
	if (charCode >= 48 && charCode <= 57)
	{
		event.keyCode = charCode;
	}
	else if (event.keyCode != 13)
		event.keyCode=null;
}