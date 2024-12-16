<template>
  <div id="app">
    <div id="contentContainer">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>


<style>
body {
    background-color: #f0f4f8;
    color: #333;
    margin: 0;
    display: grid;
    justify-content: center;
    align-items: center;
    height: 100vh;
    font-family: "Press Start 2P", system-ui;
	font-weight: 400;
	font-style: normal;
}

.container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr auto;
    row-gap: 20px;
    background-color: #ffffff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
    min-width: 700px;
    /*margin: 20px 300px 20px 300px;*/
}

.header {
    grid-column: span 2;
    text-align: center;
}

.header h1 {
    font-size: 24px;
    margin-bottom: 5px;
    color: #ff00dd;
}

.header a {
    text-decoration: none;
    color: inherit;
}

.header p {
    font-size: 14px;
    margin: 0;
}

.controls {
    grid-column: span 2;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-gap: 10px;
}

.controls label {
    font-weight: 700;
    font-size: 14px;
    margin-bottom: 5px;
}

.controls select,
.controls input[type="number"] {
    width: 80%;
    padding: 8px;
    border-radius: 5px;
    border: 1px solid #ccc;
    font-size: 14px;
}

.controls .checkbox-group {
	grid-column: span 2;
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
	grid-gap: 25px;
	justify-items: center;
    align-items: center
}

.controls .checkbox-group label {
	font-weight: 400;
	font-size: 12px;
	display: block;
	text-align: center;
}

.controls .checkbox-group input[type="checkbox"] {
    margin-right: 5px;
}

#canvasCommandLink {
    grid-column: span 2;
    margin: 0 auto;
}

canvas {
    border: 1px solid #ff00dd;
    border-radius: 10px;
    background-color: #f9f9f9;
    margin: 0 auto;
}

button, .submit-button {
    font-family: "Press Start 2P", system-ui;
	font-weight: 400;
	font-style: normal;
    grid-column: span 2;
    padding: 10px;
    background-color: #ff00dd;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

button:hover, .submit-button {
    background-color: #f700d6;
}

.result {
    grid-column: span 2;
    text-align: center;
    font-weight: bold;
    font-size: 16px;
    margin-top: 10px;
}

#resultsTable {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
	text-align: center;
	border: 1px solid #dddddd;
	border-collapse: collapse;
}	

#resultsTable > thead > tr > th{
	border: 1px solid #ff00dd;
	padding: 8px;
}
#resultsTable > tbody > tr > td {
	border: 1px solid #dddddd;
	padding: 5px;
}

.floating-message {
    position: fixed;
    top: 20px;
    right: 20px;
    background-color: #f44336;
    color: white;
    padding: 10px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    z-index: 1000;
 }
</style>
