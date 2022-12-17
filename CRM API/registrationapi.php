<?php   
require_once 'myconnection.php';  
 // $response = array to store state variables like 'error' 'message' 'user'
$response = array();  
if(isset($_GET['apicall'])){  
  switch($_GET['apicall']){  
      case 'signup': 
      if(isTheseParametersAvailable(array('firstname','lastname','email','phone','password','address','state','city','pincode','role'))){  
        $firstname = $_POST['firstname'];   
        $lastname = $_POST['lastname'];  
        $email = $_POST['email'];   
        $phone = $_POST['phone'];   
        $password = $_POST['password'];   
        $address = $_POST['address']; 
        $state = $_POST['state'];   
        $city = $_POST['city'];   
        $pincode = $_POST['pincode']; 
        $role = $_POST['role'];     

        $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");  
        $stmt->bind_param("s", $email);  
        $stmt->execute();  
        $stmt->store_result();  
// if($stmt->mysql_insert_id();))
        if($stmt->num_rows > 0){  
            $response['error'] = true;  
            $response['message'] = 'User already registered';  
            $stmt->close();  
        }  
        else{  
            $stmt = $conn->prepare("INSERT INTO users (fname, lname, email, phone,password,addr,state_name,city_name,pincode,role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt->bind_param("ssssssssss", $firstname ,$lastname, $email, $phone , $password , $address , $state , $city , $pincode , $role);  

            if($stmt->execute()){  
                $stmt = $conn->prepare("SELECT id,fname,lname,email,phone,addr,state_name,city_name,pincode,role FROM users WHERE email = ?");   
                $stmt->bind_param("s",$email);  
                $stmt->execute();  
                $stmt->bind_result($id, $firstname, $lastname,$email, $phone ,$address , $state , $city , $pincode,$role);  
                $stmt->fetch();  

                $user = array(  
                    'id'=>$id,   
                    'firstname'=>$firstname,  
                    'lastname'=>$lastname, 
                    'email'=>$email,  
                    'phone'=>$phone,
                    'address'=>$address,
                    'state'=>$state,
                    'city'=>$city,
                    'pincode'=>$pincode,
                    'role'=>$role
                );  

                $stmt->close();  

                $response['error'] = false;   
                $response['message'] = 'User registered successfully';   
                $response['user'] = $user;   
            }  
        } 

    }  
    else{  
        $response['error'] = true;   
        $response['message'] = 'required parameters are not available';   
    }  
    break; 
//------------------------------------------------------------------------------------------------------  


    case 'addUser': 
    if(isTheseParametersAvailable(array('firstname','lastname','email','phone','password','address','state','city','pincode'))){  
        $firstname = $_POST['firstname'];   
        $lastname = $_POST['lastname'];  
        $email = $_POST['email'];   
        $phone = $_POST['phone'];   
        $password = $_POST['password'];   
        $address = $_POST['address']; 
        $state = $_POST['state'];   
        $city = $_POST['city'];   
        $pincode = $_POST['pincode'];  
        $role = $_POST['role'];     

        $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");  
        $stmt->bind_param("s", $email);  
        $stmt->execute();  
        $stmt->store_result();  
// if($stmt->mysql_insert_id();))
        if($stmt->num_rows > 0){  
            $response['error'] = true;  
            $response['message'] = 'User already exist';  
            $stmt->close();  
        }  
        else{  
            $stmt = $conn->prepare("INSERT INTO users (fname, lname, email, phone,password,addr,state_name,city_name,pincode,role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt->bind_param("ssssssssss", $firstname ,$lastname, $email, $phone , $password , $address , $state , $city , $pincode, $role);  

            if($stmt->execute()){  
                $stmt = $conn->prepare("SELECT id,fname,lname,email,phone,addr,state_name,city_name,pincode , role FROM users WHERE email = ?");   
                $stmt->bind_param("s",$email);  
                $stmt->execute();  
                $stmt->bind_result($id, $firstname, $lastname,$email, $phone ,$address , $state , $city , $pincode, $role);  
                $stmt->fetch();  

                $user = array(  
                    'id'=>$id,   
                    'firstname'=>$firstname,  
                    'lastname'=>$lastname, 
                    'email'=>$email,  
                    'phone'=>$phone,
                    'address'=>$address,
                    'state'=>$state,
                    'city'=>$city,
                    'pincode'=>$pincode,
                    'role'=>$role,
                );  

                $stmt->close();  

                $response['error'] = false;   
                $response['message'] = 'User added successfully';   
                $response['user'] = $user;   
            }  
        }  

    }  
    else{  
        $response['error'] = true;   
        $response['message'] = 'required parameters are not available';   
    }  
    break; 
//------------------------------------------------------------------------------------------------------  

    case 'login':  

    if(isTheseParametersAvailable(array('email','password'))){  
        $email = $_POST['email'];  
        $password = $_POST['password'];  


        $stmt = $conn->prepare("SELECT id, fname,lname, email, phone , addr ,state_name,city_name ,pincode,role FROM users WHERE email = ? and password = ? ");  
        $stmt->bind_param("ss",$email,$password);  
        $stmt->execute();  
        $stmt->store_result(); 

        if($stmt->num_rows > 0){  
            $stmt->bind_result($id, $firstname,$lastname, $email, $phone,$address,$state,$city,$pincode,$role);  
            $stmt->fetch();  
            $user = array(  
                'id'=>$id,   
                'firstname'=>$firstname,   
                'lastname'=>$lastname,   
                'email'=>$email,  
                'phone'=>$phone,
                'address'=>$address,
                'state'=>$state,
                'city'=>$city,
                'pincode'=>$pincode ,
                'role'=>$role
            );  

            $response['error'] = false;   
            $response['message'] = 'Login successful !!';   
            $response['user'] = $user;   
        }  
        else{  
            $response['error'] = false;   
            $response['message'] = 'Invalid username or password';  
        }  
    }  
    break; 
// -----------------------------------------------------------------------------------------------------

    case 'activities': 
    // print_r($_POST);die(); 
    // print_r( $server_ip = gethostbyname(gethostname()));die();
    if(isTheseParametersAvailable(array('name','email','company','phone','address','state','city','pincode','reminderDate','assignTo','id'))){  
        $name = $_POST['name'];    
        $email = $_POST['email']; 
        $company = $_POST['company'];   
        $phone = $_POST['phone'];   
        $address = $_POST['address']; 
        $state = $_POST['state'];   
        $city = $_POST['city'];   
        $pincode = $_POST['pincode'];     
        $reminderDate = $_POST['reminderDate'];   
        $assignTo = $_POST['assignTo'];     
        $id = $_POST['id'];
        // $ref_img = $_FILES['pic'];  
        // $images = $_POST['images'];  
        // $videos = $_POST['videos'];  

      
// move_uploaded_file($ref_img['tmp_name'], UPLOAD_PATH . $ref_img['name']);
        $stmt = $conn->prepare("INSERT INTO activity (activity_name, activity_email,company_name , activity_contact ,activity_addr,state_name,city_name,pincode , reminder_date,assign_to,user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
        $stmt->bind_param("sssssssssss", $name , $email, $company , $phone , $address , $state , $city , $pincode , $reminderDate, $assignTo,$id);  

        if($stmt->execute()){  
            $last_id = $stmt->insert_id;

            $stmt_log = $conn->prepare("INSERT INTO activity_log (activity_id,activity_name, activity_email,company_name , activity_contact ,activity_addr,state_name,city_name,pincode , reminder_date,assign_to,user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt_log->bind_param("ssssssssssss", $last_id, $name , $email, $company , $phone , $address , $state , $city , $pincode , $reminderDate, $assignTo,$id); 
            $stmt_log->execute();


            $stmt->close();  



            $response['error'] = false;   
            $response['message'] = 'Activity added successfully';   
            $response['user'] = "";   

        }  

    }  
    else{  
        $response['error'] = true;   
        $response['message'] = 'required parameters are not available';   
    }  
    break; 
//---------------------------------------------------------------------------------------------------------------------------------




    case 'getExecutiveActivities':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
    $id = $_POST['id'];  
    $assign_to = $_POST['assign_to'];

    $stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to , status FROM activity where assign_to = ? ");  
    $stmt->bind_param("s",$assign_to);  
    $stmt->execute();  
    $stmt_result = $stmt->get_result();
    if($stmt_result->num_rows > 0){  
        $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo,$status);  
        $activity = array();
        while($row_data = $stmt_result->fetch_assoc()){
         $temp = array();
         $temp['id'] = $row_data['activity_id']; 
         $temp['name'] = $row_data['activity_name']; 
         $temp['phone'] = $row_data['activity_contact']; 
         $temp['address'] = $row_data['activity_addr']; 
         $temp['state'] = $row_data['state_name']; 
         $temp['city'] = $row_data['city_name']; 
         $temp['pincode'] = $row_data['pincode']; 
         $temp['date'] = $row_data['reminder_date']; 
         $temp['company'] = $row_data['company_name']; 
         $temp['email'] = $row_data['activity_email']; 
         $temp['assignTo'] = $row_data['assign_to']; 
         $temp['status'] = $row_data['status']; 

         array_push($activity, $temp);


     }

     $response['error'] = false;   
     $response['message'] = 'Executive Activities Fetch successfull';   
     $response['activity'] = $activity;   
 }  
 else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'getAdminActivities':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$id = $_POST['id'];  
$assign_to = $_POST['assign_to'];

$stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to , status FROM activity where user_id = ? ");  
$stmt->bind_param("s",$id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo,$status);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['assignTo'] = $row_data['assign_to']; 
     $temp['status'] = $row_data['status']; 

     array_push($activity, $temp);

 }

 $response['error'] = false;   
 $response['message'] = 'Admin Activities Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------
case 'adminActivityCount':  
$id = $_POST['id'];
$stmt = $conn->prepare("SELECT COUNT(user_id) FROM activity where user_id = ? ");
$stmt->bind_param("s",$id);  
$stmt->execute();  

$stmt->bind_result($count);  
$stmt->fetch();  
if($count>=0){
    $response['error'] = false;   
    $response['message'] = 'Activities Fetch successfull';   
    $response['count'] = $count;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   


//-----------------------------------------------------------------------------------

case 'executiveActivityCount':  
$assign_to = $_POST['assign_to'];
$stmt = $conn->prepare("SELECT COUNT(assign_to) FROM activity where assign_to = ? ");
$stmt->bind_param("s",$assign_to);  
$stmt->execute();  
$stmt->bind_result($count);  
$stmt->fetch();  
if($count>=0){
    $response['error'] = false;   
    $response['message'] = 'Activities Fetch successfull';   
    $response['count'] = $count;   
}  

else
{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}    
break;   


//-----------------------------------------------------------------------------------

case 'executiveActivitiesStatusCount':
$assignTo = $_POST['assignTo'];

$status_inprogress = 'in progress';
$status_completed = 'completed';
$status_cancelled = 'cancelled';


$stmt_inprogress = $conn->prepare("SELECT COUNT(activity_id) FROM activity where assign_to = ? AND status = ?");
$stmt_inprogress->bind_param("ss",$assignTo,$status_inprogress); 

if($stmt_inprogress->execute())
{
    $stmt_inprogress -> store_result();
    $stmt_inprogress->bind_result($inprogresscount);
    $stmt_inprogress -> fetch();
}

$stmt_completed = $conn->prepare("SELECT COUNT(activity_id) FROM activity where assign_to = ? AND status = ?");
$stmt_completed->bind_param("ss",$assignTo,$status_completed);

if($stmt_completed->execute())
{
    $stmt_completed -> store_result();
    $stmt_completed->bind_result($completedcount);  
    $stmt_completed -> fetch();
}

$stmt_cancelled = $conn->prepare("SELECT COUNT(activity_id) FROM activity where assign_to = ? AND status = ?");
$stmt_cancelled->bind_param("ss",$assignTo,$status_cancelled);
if($stmt_cancelled->execute()){
   $stmt_cancelled -> store_result();
   $stmt_cancelled->bind_result($cancelledcount);  
   $stmt_cancelled -> fetch();
}


$response['error'] = false;   
$response['message'] = 'Count Fetch successfull'; 
$response['inprogresscount'] = $inprogresscount;    
$response['completedcount'] = $completedcount;    
$response['cancelledcount'] = $cancelledcount;    


break;   
//-----------------------------------------------------------------------------------


case 'adminActivitiesStatusCount':
$id = $_POST['id'];

$status_inprogress = 'in progress';
$status_completed = 'completed';
$status_cancelled = 'cancelled';


$stmt_inprogress = $conn->prepare("SELECT COUNT(activity_id) FROM activity where user_id = ? AND status = ?");
$stmt_inprogress->bind_param("ss",$id,$status_inprogress); 

if($stmt_inprogress->execute())
{
    $stmt_inprogress -> store_result();
    $stmt_inprogress->bind_result($inprogresscount);
    $stmt_inprogress -> fetch();
}

$stmt_completed = $conn->prepare("SELECT COUNT(activity_id) FROM activity where user_id = ? AND status = ?");
$stmt_completed->bind_param("ss",$id,$status_completed);

if($stmt_completed->execute())
{
    $stmt_completed -> store_result();
    $stmt_completed->bind_result($completedcount);  
    $stmt_completed -> fetch();
}

$stmt_cancelled = $conn->prepare("SELECT COUNT(activity_id) FROM activity where user_id = ? AND status = ?");
$stmt_cancelled->bind_param("ss",$id,$status_cancelled);
if($stmt_cancelled->execute()){
   $stmt_cancelled -> store_result();
   $stmt_cancelled->bind_result($cancelledcount);  
   $stmt_cancelled -> fetch();
}


$response['error'] = false;   
$response['message'] = 'Count Fetch successfull'; 
$response['inprogresscount'] = $inprogresscount;    
$response['completedcount'] = $completedcount;    
$response['cancelledcount'] = $cancelledcount;    


break;   
//-----------------------------------------------------------------------------------


case 'getUsers':  

  // if(isTheseParametersAvailable(array('id'))){  
   // WHERE city_name = ?

$stmt = $conn->prepare("SELECT id, fname , lname FROM users ");  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($id,$fname,$lname);  
    $users = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['id']; 
     $temp['fname'] = $row_data['fname']; 
     $temp['lname'] = $row_data['lname']; 
     array_push($users, $temp);
 }

 $response['error'] = false;   
 $response['message'] = 'users Fetch successfull';   
 $response['users'] = $users;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'editActivities':  
if(isTheseParametersAvailable(array('act_id','name','email','company','phone','address','state','city','pincode','reminderDate','assignTo','id','status'))){ 
    $act_id = $_POST['act_id'];    
    $name = $_POST['name'];    
    $email = $_POST['email']; 
    $company = $_POST['company'];   
    $phone = $_POST['phone'];   
    $address = $_POST['address']; 
    $state = $_POST['state'];   
    $city = $_POST['city'];   
    $pincode = $_POST['pincode'];     
    $reminderDate = $_POST['reminderDate'];   
    $assignTo = $_POST['assignTo'];     
    $id = $_POST['id'];       
    $status = $_POST['status'];       



    
    $stmt = $conn->prepare("UPDATE activity SET activity_name=?, activity_email=?,company_name=? , activity_contact=? ,activity_addr=?,state_name=?,city_name=?,pincode=? , reminder_date=?,assign_to=?,status=? 
      where activity_id = ? ");
    $stmt->bind_param("ssssssssssss", $name,$email,$company,$phone,$address,$state,$city,$pincode,$reminderDate,$assignTo,$status,$act_id);  
    if($stmt->execute()){       
     $stmt_log = $conn->prepare("INSERT INTO activity_log (activity_id,activity_name, activity_email,company_name , activity_contact ,activity_addr,state_name,city_name,pincode , reminder_date,assign_to,user_id,status) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
     $stmt_log->bind_param("sssssssssssss", $act_id,$name , $email, $company , $phone , $address , $state , $city , $pincode , $reminderDate, $assignTo,$id,$status);
     $stmt_log->execute();


     $stmt->close();  



     $response['error'] = false;   
     $response['message'] = 'Activity edited successfully';   
             // $response['user'] = ;   

 }  

}  
else{  
    $response['error'] = true;   
    $response['message'] = 'required parameters are not available';   
}  
break; 
//---------------------------------------------------------------------------------------------------------------------------------
case 'getActivitiesReminderNotificationData':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where  assign_to = ? AND  DATE(reminder_date) BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) ");  
$stmt->bind_param("s",$assign_to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['userid'] = $row_data['user_id']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 


     array_push($activity, $temp);


 }

 $response['error'] = false;   
 $response['message'] = 'Reminder date data Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------------------------------------

case 'getAdminUpcomingActivitiesCount':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$user_id = $_POST['user_id'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where  user_id = ? AND  DATE(reminder_date) BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) ");  
$stmt->bind_param("s",$user_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['userid'] = $row_data['user_id']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 


     array_push($activity, $temp);


 }

 $response['error'] = false;   
 $response['message'] = 'Reminder date data Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'getAdminHistory':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$id = $_POST['id'];  
$assign_to = $_POST['assign_to'];
$act_id = $_POST['act_id'];


$stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to FROM activity_log where user_id = ? and activity_id=?");  
$stmt->bind_param("ss",$id,$act_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['assignTo'] = $row_data['assign_to']; 

     array_push($activity, $temp);

 }

 $response['error'] = false;   
 $response['message'] = 'Admin activity history Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'getExecutiveHistory':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$id = $_POST['id'];  
$assign_to = $_POST['assign_to'];
$act_id = $_POST['act_id'];


$stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to FROM activity_log where assign_to = ? and activity_id=? ");  
$stmt->bind_param("ss",$assign_to,$act_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['assignTo'] = $row_data['assign_to']; 

     array_push($activity, $temp);

 }

 $response['error'] = false;   
 $response['message'] = 'Executive activity history Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'uploadImage': 
    // print_r($_POST);die(); 
    // print_r( $server_ip = gethostbyname(gethostname()));die();
    if(isset($_FILES['imageFile'])){  
        $ref_img = $_FILES['imageFile'];  
      
move_uploaded_file($ref_img['tmp_name'], IMAGE_UPLOAD_PATH . $ref_img['name']);            
$response['error'] = false;   
            $response['message'] = 'image uploaded successfully';   
            $response['image_name'] = $_FILES['imageFile']['name'];   
        }  
 
    else{  
        $response['error'] = true;   
        $response['message'] = 'required parameters are not available';   
    }  
    break; 




  
 //---------------------------------------------------------------------------------------------------------------------------------


   case 'uploadVideo': 
    // print_r($_POST);die(); 
    // print_r( $server_ip = gethostbyname(gethostname()));die();
  

  if(isset($_FILES['video'])){  
        $ref_vid = $_FILES['video'];  
      
move_uploaded_file($ref_vid['tmp_name'], VIDEO_UPLOAD_PATH . $ref_vid['name']);            
$response['error'] = false;   
            $response['message'] = 'video uploaded successfully';   
            $response['video_name'] = $_FILES['video']['name'];   
        }  
 
    else{  
        $response['error'] = true;   
        $response['message'] = 'required parameters are not available';   
    }  
    break; 
 //---------------------------------------------------------------------------------------------------------------------------------
case 'getExecutiveDateRangedData':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];
$from = $_POST['from'];
$to = $_POST['to'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where assign_to = ? AND DATE(fromDate) = ? BETWEEN DATE(toDate) = ? ");  
$stmt->bind_param("sss",$assign_to,$from,$to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['userid'] = $row_data['user_id']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 


     array_push($activity, $temp);


 }

 $response['error'] = false;   
 $response['message'] = 'executive range date , data Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'getAdminDateRangedData':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];
$from = $_POST['from'];
$to = $_POST['to'];


$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where  assign_to = ? AND  DATE(fromDate)=? BETWEEN DATE(toDate)=? ");  
$stmt->bind_param("sss",$assign_to,$from,$to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['userid'] = $row_data['user_id']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 


     array_push($activity, $temp);


 }

 $response['error'] = false;   
 $response['message'] = 'admin range date , data Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

 case 'getTodaysExecutiveEvents':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
    $assign_to = $_POST['assign_to'];
    $today = $_POST['today'];


    $stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to FROM activity where assign_to = ? and reminder_date= ?");  
    $stmt->bind_param("ss",$assign_to,$today);  
    $stmt->execute();  
    $stmt_result = $stmt->get_result();
    if($stmt_result->num_rows > 0){  
        $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo);  
        $activity = array();
        while($row_data = $stmt_result->fetch_assoc()){
         $temp = array();
         $temp['id'] = $row_data['activity_id']; 
         $temp['name'] = $row_data['activity_name']; 
         $temp['phone'] = $row_data['activity_contact']; 
         $temp['address'] = $row_data['activity_addr']; 
         $temp['state'] = $row_data['state_name']; 
         $temp['city'] = $row_data['city_name']; 
         $temp['pincode'] = $row_data['pincode']; 
         $temp['date'] = $row_data['reminder_date']; 
         $temp['company'] = $row_data['company_name']; 
         $temp['email'] = $row_data['activity_email']; 
         $temp['assignTo'] = $row_data['assign_to']; 

         array_push($activity, $temp);


     }

     $response['error'] = false;   
     $response['message'] = 'Todays Executive events Fetch successfull';   
     $response['activity'] = $activity;   
 }  
 else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'getTodaysAdminEvents':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$id = $_POST['id'];  
$today = $_POST['today'];

$stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to FROM activity where user_id = ? and reminder_date = ? ");  
$stmt->bind_param("ss",$id,$today);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$phone,$address, $state, $city,$pincode,$date,$company,$email,$assignTo);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['id'] = $row_data['activity_id']; 
     $temp['name'] = $row_data['activity_name']; 
     $temp['phone'] = $row_data['activity_contact']; 
     $temp['address'] = $row_data['activity_addr']; 
     $temp['state'] = $row_data['state_name']; 
     $temp['city'] = $row_data['city_name']; 
     $temp['pincode'] = $row_data['pincode']; 
     $temp['date'] = $row_data['reminder_date']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['assignTo'] = $row_data['assign_to']; 

     array_push($activity, $temp);

 }

 $response['error'] = false;   
 $response['message'] = 'Todays Admin events Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

case 'adminTodaysEventsCount':  
$id = $_POST['id'];
$today = $_POST['today'];
$stmt = $conn->prepare("SELECT COUNT(user_id) FROM activity where user_id = ? and reminder_date = ?");
$stmt->bind_param("ss",$id,$today);  
$stmt->execute();  

$stmt->bind_result($count);  
$stmt->fetch();  
if($count>=0){
    $response['error'] = false;   
    $response['message'] = 'Admin Todays Events count Fetch successfull';   
    $response['count'] = $count;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   


//-----------------------------------------------------------------------------------

case 'executiveTodaysEventsCount':  
$assign_to = $_POST['assign_to'];
$today = $_POST['today'];
$stmt = $conn->prepare("SELECT COUNT(assign_to) FROM activity where assign_to = ? and reminder_date = ? ");
$stmt->bind_param("ss",$assign_to , $today);  
$stmt->execute();  
$stmt->bind_result($count);  
$stmt->fetch();  
if($count>=0){
    $response['error'] = false;   
    $response['message'] = 'Executive todays events count  Fetch successfull';   
    $response['count'] = $count;   
}  

else
{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}    
break;   


//-----------------------------------------------------------------------------------


default:   
$response['error'] = true;   
$response['message'] = 'Invalid Operation Called';  
}  
}  
else{  
 $response['error'] = true;   
 $response['message'] = 'Invalid API Call';  
}  
echo json_encode($response);  
function isTheseParametersAvailable($params){  
    foreach($params as $param){  
     if(!isset($_POST[$param])){  
         return false;   
     }  
 }  
 return true;   
}  
?>  













