/** Fetches messages and add them to the page. */
function fetchProfile() {
	console.log('user-page-loader.js -- fetchProfile');
	const url = '/updateName';
	fetch(url)
		.then((response) => {
			return response.json();
		})
		.then((new_name) => {
            console.nameContainer = document.getElementById('name-container');
            
			nameContainer.innerHTML = new_name;
			
		});
}