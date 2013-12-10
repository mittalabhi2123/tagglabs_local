$(document).ready(function () {
  
$('.form-control, .form-control-mobile').blur(function(e) {
 
	var s = $(this).val();
	 
	if (/^[0-9]{1,10}$/.test(+s) && s.length==10) {
	  $(this).parent().find("#validator-img").attr('src','images/2/verified_button.png')
		$(this).parent().find("#validator-img").attr("width","54");
	    $(this).parent().find(".onsucess").show();

	}else{
	     $(this).parent().find("#validator-img").attr('src','images/2/error_on_button.png');
        $(this).parent().find("#validator-img").attr("width","160")
	}
 
});
 
})
