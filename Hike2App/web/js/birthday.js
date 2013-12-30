$(document).ready(function () {
  	//$(".onsucess").show();

 $('.fb-login').click(function(){
        
        var url = "http://54.200.86.247:8084/invitation/facebookLogin";
		    $.getJSON(url, function(data){
		       console.log(data.url);
                       window.location.href = data.url;
	        });
        
        
        
    })


$('.mobile-top, .mobile-bottom').blur(function(e) {
    
 
 
        var s = $(this).val();
	 
	 
	if (/^[0-9]{1,10}$/.test(+s) && $(this).val().length==10) {
	  $(this).parent().find("#validator-img").attr('src','images/2/verified_button.png')
		$(this).parent().find("#validator-img").attr("width","54");


			if($(this).hasClass('mobile-top')){

					var url = "http://54.200.86.247:8084/invitation/verify?phoneNo="+s;
					    $.getJSON(url, function(data) {
			 				  
						})
						.success(function(data) { 
							

							 var responseOwner = data.response;
                                                         
								if(responseOwner == "PRIMARY"){

									$(".onsucess").show();

								}else if(responseOwner == "FRIEND"){

									 

									window.location = "table4.html"
								}else if(responseOwner == "INVALID"){

									 
                                                                        
                                                                        $(".onsucess").hide();
                                                                        $(".oops").show();

								}
						 })
						.error(function() {   })
						.complete(function() {   });
			}

			if($(this).hasClass('mobile-bottom')){ 
                             var friendNo =  $(this).val();  
                                 
                                var s = $(this).parents('.center').find('.mobile-top').val();
                                
                                 var friendNo =  $(this).val();  
                                var url = "http://54.200.86.247:8084/invitation/saveFrndPhone?phoneNo="+s+"&friend_uid=abc&friend_phone="+friendNo;
				 $.getJSON(url, function(data) {
			 				 window.location = "table3.html";
						})
						.success(function(data) { 
							
                                                       window.location = "table3.html";

							 
						 })
						.error(function() {   })
						.complete(function() {   });



			}

	}else{
	     $(this).parent().find("#validator-img").attr('src','images/2/error_on_button.png');
        $(this).parent().find("#validator-img").attr("width","160")
	}
 
});
 
})
