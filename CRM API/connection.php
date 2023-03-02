<?php  
$servername = "localhost";  
$username = "root";  
$password = "";  
#below is our db name on mysql --- comment
$database = "crm";  
$conn = new mysqli($servername, $username, $password, $database);  
if ($conn->connect_error) {  
    die("Connection failed: " . $conn->connect_error);  
}  

// define("IMGPATH", "https://affetta.com/cms/");
 ?> 

