/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
	window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
	document.getElementById('page-title').innerText = parameterUsername;
	document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfViewingSelf() {
	fetch('/login-status')
		.then((response) => {
			return response.json();
		})
		.then((loginStatus) => {
			document.getElementById('about-me-form').classList.remove('hidden');
			if (loginStatus.isLoggedIn &&
				loginStatus.username == parameterUsername) {
				fetchBlobstoreUrlAndShowForm();
				

			}
		});
}

/* Fetches the Blobstore URL (where the image will be stored) then displays the form */
function fetchBlobstoreUrlAndShowForm() {
	fetch('/blobstore-upload-url')
		.then((response) => {
			return response.text();
		})
		.then((imageUploadUrl) => {
			console.log('imageUploadUrl is: ' + imageUploadUrl);
			const messageForm = document.getElementById('message-form');
			messageForm.action = imageUploadUrl;
			messageForm.classList.remove('hidden');
		});
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
	console.log('user-page-loader.js -- fetchMessages');
	const url = '/messages?user=' + parameterUsername;
	fetch(url)
		.then((response) => {
			return response.json();
		})
		.then((messages) => {
			console.log(messages);
			const messagesContainer = document.getElementById('message-container');
			if (messages.length == 0) {
				messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
			} else {
				messagesContainer.innerHTML = '';
			}
			messages.forEach((message) => {
				const messageDiv = buildMessageDiv(message);
				messagesContainer.appendChild(messageDiv);
			});
		});
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
	// console.log('user-page-loader.js -- buildMessageDiv');
	// const headerDiv = document.createElement('div');
	// headerDiv.classList.add('message-header');
	// headerDiv.appendChild(document.createTextNode(
	// message.user + ' - ' + new Date(message.timestamp)));

	// const bodyDiv = document.createElement('div');
	// bodyDiv.classList.add('message-body');
	// var messageText = message.text;
	// bodyDiv.innerHTML = replaceImageAddressWithHTML(messageText);

	// console.log(message.imageUrl);
	// const imageUrl = message.imageUrl;
	
	// // 1st check checks for null, undefined, empty strings
	// // 2nd check check if string is made up  of only white spaces
	// if (Boolean(imageUrl) && !!imageUrl.trim()) {
	// 	console.log('inside if');
	// 	const image = document.createElement('img');
	// 	image.src = imageUrl;
	// 	console.log(image);
	// 	bodyDiv.appendChild(image);
	// }
	
	// const messageDiv = document.createElement('div');
	// messageDiv.classList.add('message-div');
	// messageDiv.appendChild(headerDiv);
	// messageDiv.appendChild(bodyDiv);

	// return messageDiv;

	const wrapper = document.createElement('div'); 
    wrapper.classList.add('card');
    wrapper.style = 'width: 70rem'; 

    const inner_wrapper = document.createElement('div'); 
    inner_wrapper.classList.add('card-body'); 
    wrapper.appendChild(inner_wrapper); 

    const card_title = document.createElement('h4');
    card_title.classList.add('card-title'); 
    card_title.appendChild(document.createTextNode(message.user)); 
    inner_wrapper.appendChild(card_title); 

    const card_list = document.createElement('ul');
    card_list.classList.add('list-group');
    card_list.classList.add('list-group-flush'); 
    wrapper.appendChild(card_list); 

    const time_li = document.createElement('li'); 
    time_li.classList.add('list-group-item'); 
    time_li.appendChild(document.createTextNode(new Date(message.timestamp))); 
    card_list.appendChild(time_li);

    const message_li = document.createElement('li'); 
    message_li.classList.add('list-group-item'); 
    message_li.appendChild(document.createTextNode(message.text)); 
    card_list.appendChild(message_li); 

    return wrapper;
}

/** Replace image links with the img HTML tag*/
function replaceImageAddressWithHTML(text) {
	const regex = /(https?:\/\/.*\.(?:png|jpg))/i;
	const replacement = '<a href="$&" target="_blank">$&</a>';
	const result = text.replace(regex, replacement);
	return result;
}
/** fetches about me  */
function fetchAboutMe(){
	const url = '/about?user=' + parameterUsername;
	fetch(url).
		then((response) => {
	  return response.json();
	}).then((new_name) => {

		const nameContainer = document.getElementById('name-container');	  

		if (new_name.length == 0) {
			nameContainer.innerHTML = 'Please enter name ';
		} else{

			console.log(new_name);
	  
	  		nameContainer.innerHTML = new_name.name;
		}

		
  
	});
  }
/** Fetches data and populates the UI of the page. */
function buildUI() {
	//setPageTitle();
	showMessageFormIfViewingSelf();
	fetchMessages();
	fetchAboutMe();
}