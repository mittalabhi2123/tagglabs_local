        // form wizard
        $( function() {
            var $signupForm = $( '#SignupForm' );
            
            $signupForm.validationEngine();
            
            $signupForm.formToWizard({
                submitButton: 'SaveAccount',
                showProgress: true, //default value for showProgress is also true
                nextBtnName: 'Forward >>',
                prevBtnName: '<< Previous',
                showStepNo: true,
                validateBeforeNext: function() {
                    return $signupForm.validationEngine( 'validate' );
                }
            });
            
            
            $( '#txt_stepNo' ).change( function() {
                $signupForm.formToWizard( 'GotoStep', $( this ).val() );
            });
            
            $( '#btn_next' ).click( function() {
                $signupForm.formToWizard( 'NextStep' );
            });
            
            $( '#btn_prev' ).click( function() {
                $signupForm.formToWizard( 'PreviousStep' );
            });
        });


        // show hide toggle
animatedcollapse.addDiv('all-devices-result', 'fade=0,speed=400,group=customersegment')
animatedcollapse.addDiv('input1', 'fade=0,speed=400,group=customersegment,persist=1,hide=1')
animatedcollapse.addDiv('new-segment-result', 'fade=0,speed=400,group=customersegment,hide=1')
animatedcollapse.addDiv('one-device-result', 'fade=0,speed=400,group=customersegment,hide=1')

animatedcollapse.addDiv('launch-app-result', 'fade=0,speed=400,group=cta')
animatedcollapse.addDiv('launch-url-result', 'fade=0,speed=400,group=cta,persist=1,hide=1')
animatedcollapse.addDiv('open-app-screen-result', 'fade=0,speed=400,group=cta,hide=1')

animatedcollapse.addDiv('less-less-equal', 'fade=0,speed=400,group=parameters')
animatedcollapse.addDiv('more-less-equal', 'fade=0,speed=400,group=parameters,persist=1,hide=1')
animatedcollapse.addDiv('more-more-equal', 'fade=0,speed=400,group=parameters,persist=1,hide=1')

animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
	//$: Access to jQuery
	//divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
	//state: "block" or "none", depending on state
}

animatedcollapse.init()

