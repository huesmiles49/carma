/**
 * JavaScript File that shows the new view of the lister view after they click on the submit button
 * Also will read the checkbox and see if they picked the parking structure
 */
function showListerViewPart2(){
	document.getElementById('listerViewPart1').style.display = 'none';
	document.getElementById('listerViewPart2').style.display = 'block';
}

function showListerViewPart1(){
	document.getElementById('listerViewPart2').style.display = 'none';
	document.getElementById('listerViewPart1').style.display = 'block';
}

function successMessage(){
	if(document.getElementById('listerViewPart2').style.display = 'block'){
		out.println("ok");
	}
}



function CheckLocation(val){
 var element=document.getElementById('level');
 if(val=='StructA'||val=='StructB'||val=='StructC')
   element.style.display='block';
 else  
   element.style.display='none';
}