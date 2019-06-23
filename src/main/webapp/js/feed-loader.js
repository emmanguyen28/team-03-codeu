// fetch messages and add them to the page 
// by default, fetch uses GET 
function fetchMessages() {
	const url = '/feed';
	fetch(url).then((response) => {
		return response.json(); // parse the returned json  
	}).then((messages) => {
		const messageContainer = document.getElementById('message-container');
		if (messages.length == 0) {
			messageContainer.innerHTML = '<p>There are no posts yet.</p>';
		} else {
			messageContainer.innerHTML = '';
		}
		messages.forEach((message) => {
			const messageDiv = buildMessageDiv(message);
			messageContainer.appendChild(messageDiv);
		});
	});
}

function buildMessageDiv(message) {
	const usernameDiv = document.createElement('div');
	usernameDiv.classList.add("left-align");
	usernameDiv.appendChild(document.createTextNode(message.user));

	const timeDiv = document.createElement('div');
	timeDiv.classList.add('right-align');
	timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

	const headerDiv = document.createElement('div');
	headerDiv.classList.add('message-header');
	headerDiv.appendChild(usernameDiv);
	headerDiv.appendChild(timeDiv);

	const bodyDiv = document.createElement('div');
	bodyDiv.classList.add('message-body');
	bodyDiv.innerHTML = replaceImageAddressWithAnchorTag(message.text);

	console.log('about to take care of image');
	const imageUrl = message.imageUrl;
	// 1st check checks for null, undefined, empty strings
	// 2nd check check if string is made up  of only white spaces
	if (Boolean(imageUrl) && !!imageUrl.trim()) {
		console.log('inside if');
		const image = document.createElement('img');
		image.src = imageUrl;
		bodyDiv.appendChild(image);
	}

	const messageDiv = document.createElement('div');
	messageDiv.classList.add("message-div");
	messageDiv.appendChild(headerDiv);
	messageDiv.appendChild(bodyDiv);

	return messageDiv;
}

/** Replace image links with the img HTML tag*/
function replaceImageAddressWithAnchorTag(text) {
	const regex = /(https?:\/\/.*\.(?:png|jpg))/i;
	const replacement = '<a href="$&" target="_blank">$&</a>';
	const result = text.replace(regex, replacement);
	console.log(result);
	return result;
}


// fetch data and populate the UI of the page 
function buildUI() {
	fetchMessages();
}
