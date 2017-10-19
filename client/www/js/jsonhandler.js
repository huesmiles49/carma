/**
 *  This file will have all the json conversions needed
 */

/**
 * Converts the form with id=formName into JSON and returns it
 * @formName the form with id=formName to use
 * @returns JSON of the form with id=formName
 */
function convertFormToJSON(formName) {
	var myForm = document.getElementById(formName);

	var formData = new FormData(myForm),
		result = {};

	for (var entry of formData.entries()) {
		result[entry[0]] = entry[1];
	}
	result = JSON.stringify(result);
	console.log(result);

	return result;
}