/**
 * This file will provide controls for the signup page
 */

/**
 * Expected to be called by the button on the firstPage
 * Will hide the firstPage and show the secondPage
 * @returns nothing
 */
function showSecondPage() {
    document.getElementById('firstPage').style.display = 'none';
    document.getElementById('secondPage').style.display ='block';
}

/**
 * Expected to be called by the button on the secondPage
 * Will hide the secondPage and show the thirdPage
 * @returns nothing
 */
function showThirdPage() {
	document.getElementById('secondPage').style.display = 'none';
	document.getElementById('thirdPage').style.display = 'block';
}