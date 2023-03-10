<?php  
$servername = "localhost";  
$username = "root";  
$password = "";  
#below is our db name on mysql --- comment
$database = "crm_new";  
$conn = new mysqli($servername, $username, $password, $database);  
if ($conn->connect_error) {  
    die("Connection failed: " . $conn->connect_error);  
}  

 define("IMGPATH", "https://affetta.com/cms/");
  define("UPLOADPATH", "http://192.168.0.107/crm/");
 // define("UPLOADPATH", "https://ecolods.com/crm_api/");


  // define("VIDEO_UPLOAD_PATH", "videos/");
  // define("UPLOADPATH", "http://192.168.0.104/affetta_api/");

 ?> 

