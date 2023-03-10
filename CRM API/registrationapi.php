<?php   
require_once 'myconnection.php';  
 // $response = array to store state variables like 'error' 'message' 'user'
$response = array();  
if(isset($_GET['apicall'])){  
  switch($_GET['apicall']){  
      case 'signup': 
      if(isTheseParametersAvailable(array('firstname','lastname','email','phone','password','address','state','city','pincode','role'))){  
        date_default_timezone_set('Asia/Kolkata'); 
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
        $created_on = date('Y-m-d H:i:s');

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
            $stmt = $conn->prepare("INSERT INTO users (fname, lname, email, phone,password,addr,state_name,city_name,pincode,role,created_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt->bind_param("sssssssssss", $firstname ,$lastname, $email, $phone , $password , $address , $state , $city , $pincode , $role,$created_on);  

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
        date_default_timezone_set('Asia/Kolkata'); 
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
        $added_by = $_POST['added_by']; 
        $created_on = date('Y-m-d H:i:s');
    

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
            $stmt = $conn->prepare("INSERT INTO users (fname, lname, email, phone,password,addr,state_name,city_name,pincode,role,created_on,added_by) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt->bind_param("ssssssssssss", $firstname ,$lastname, $email, $phone , $password , $address , $state , $city , $pincode, $role,$created_on,$added_by);  

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
        date_default_timezone_set('Asia/Kolkata'); 
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
        $created_on = date('Y-m-d H:i:s');

        // $ref_img = $_FILES['pic'];  
        // $images = $_POST['images'];  
        // $videos = $_POST['videos'];  

      
// move_uploaded_file($ref_img['tmp_name'], UPLOAD_PATH . $ref_img['name']);
        $stmt = $conn->prepare("INSERT INTO activity (activity_name, activity_email,company_name , activity_contact ,activity_addr,state_name,city_name,pincode , reminder_date,assign_to,user_id,created_on,modified_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
        $stmt->bind_param("sssssssssssss", $name , $email, $company , $phone , $address , $state , $city , $pincode , $reminderDate, $assignTo,$id,$created_on,$created_on);  

        if($stmt->execute()){  
            $last_id = $stmt->insert_id;

            $stmt_log = $conn->prepare("INSERT INTO activity_log (activity_id,activity_name, activity_email,company_name , activity_contact ,activity_addr,state_name,city_name,pincode , reminder_date,assign_to,user_id,created_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            $stmt_log->bind_param("sssssssssssss", $last_id, $name , $email, $company , $phone , $address , $state , $city , $pincode , $reminderDate, $assignTo,$id,$created_on); 
            $stmt_log->execute();


            $stmt->close();  



            $response['error'] = false;   
            $response['message'] = 'Activity added successfully';   
            $response['user'] = "";  
             $response['last_id'] = $last_id;  


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

$stmt = $conn->prepare("SELECT activity_id, activity_name,activity_contact,activity_addr, state_name, city_name , pincode , reminder_date , company_name , activity_email , assign_to , status FROM activity");  
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

$stmt = $conn->prepare("SELECT id, fname , lname FROM users ORDER BY fname ASC ");  
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
          date_default_timezone_set('Asia/Kolkata'); 
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
        $modified_on = date('Y-m-d H:i:s');



    
    $stmt = $conn->prepare("UPDATE activity SET activity_name=?, activity_email=?,company_name=? , activity_contact=? ,activity_addr=?,state_name=?,city_name=?,pincode=? , reminder_date=?,assign_to=?,status=?,modified_on=? 
      where activity_id = ? ");
    $stmt->bind_param("sssssssssssss", $name,$email,$company,$phone,$address,$state,$city,$pincode,$reminderDate,$assignTo,$status,$modified_on,$act_id);  
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
case 'getExecutiveInProgressData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where  assign_to = ? AND status='in progress' ");  
$stmt->bind_param("s",$assign_to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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
case 'getExecutiveCompletedData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where  assign_to = ? AND status='completed' ");  
$stmt->bind_param("s",$assign_to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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
case 'getExecutiveCancelledData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where  assign_to = ? AND status='cancelled' ");  
$stmt->bind_param("s",$assign_to);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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
case 'getAdminInProgressData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$user_id = $_POST['user_id'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where   user_id = ? AND status='in progress' ");  
$stmt->bind_param("s",$user_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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
case 'getAdminCompletedData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$user_id = $_POST['user_id'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where   user_id = ? AND status='completed' ");  
$stmt->bind_param("s",$user_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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

case 'getAdminCancelledData':   

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$user_id = $_POST['user_id'];

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where   user_id = ? AND status='cancelled' ");  
$stmt->bind_param("s",$user_id);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
     $temp['status'] = $row_data['status']; 
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 

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




case 'getAdminUpcomingActivitiesCount':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$user_id = $_POST['user_id'];
$progress = "in progress";

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status,activity_email,company_name,assign_to from activity where  user_id = ? AND status =?  AND  DATE(reminder_date) BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) ");  
$stmt->bind_param("ss",$user_id,$progress);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status,$email,$company,$assign_to);  
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
    $temp['status'] = $row_data['status'];
     $temp['email'] = $row_data['activity_email']; 
     $temp['company'] = $row_data['company_name']; 
     $temp['assign_to'] = $row_data['assign_to']; 


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
case 'getExecutiveUpcomingActivitiesCount':  

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$assign_to = $_POST['assign_to'];
$progress = "in progress";

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode,status from activity where  assign_to = ? AND status =? AND  DATE(reminder_date) BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) ");  
$stmt->bind_param("ss",$assign_to,$progress);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($activity_id,$name,$date,$userid,$phone,$address,$state,$city,$pincode,$status);  
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
    $temp['status'] = $row_data['status']; 


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


//-----------------------------------------------------------------------------------------------------------------------
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

$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where assign_to = ? AND (DATE(reminder_date) BETWEEN ? AND ?) ");  
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
$id = $_POST['id'];
$from = $_POST['from'];
$to = $_POST['to'];


$stmt = $conn->prepare("SELECT activity_id,activity_name,reminder_date , user_id,activity_contact,activity_addr,state_name,city_name,pincode from activity where user_id = ? AND (DATE(reminder_date) BETWEEN ? AND ?) ");  
$stmt->bind_param("sss",$id,$from,$to);  
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


 case 'forgotPassword':  

    if(isTheseParametersAvailable(array('email','phone'))){  
        $email = $_POST['email'];  
        $phone = $_POST['phone'];  


        $stmt = $conn->prepare("SELECT id, password FROM users WHERE phone = ? and email = ? ");  
        $stmt->bind_param("ss",$phone,$email);  
        $stmt->execute();  
        $stmt->store_result(); 

        if($stmt->num_rows > 0){  
            $stmt->bind_result($id, $password);  
            $stmt->fetch();  
            $user = array(  
                'id'=>$id,   
                'password'=>$password    
            );  

            $response['error'] = false;   
            $response['message'] = 'Details Matched , Congratulations!!';   
            $response['user'] = $user;   
        }  
        else{  
            $response['error'] = false;   
            $response['message'] = 'Either or both email and phone incorrect';  
        }  
    }  
    break; 
// -----------------------------------------------------------------------------------------------------
 case 'sendPassword':  

        $password = $_POST['password'];  
        $to = $_POST['tos'];  
        $email = "myecolods@gmail.com";
        $subject = "Password Recovery";
        $headers = "From: $firstname";
        $message = " Your password is ".$password;
        $firstname = "CRM";
        $lastname = "App";
        $headers .= " : $subject ".$email."\r\n";
        $body ="Hello Sir,\n\nFROM: $firstname $lastname\n\nE-MAIL: $email\n\nSUBJECT: $subject\n\nMESSAGE: $message";
        
// If there are no errors, send the email

    $success = mail ($to, $subject, $body, $headers);
    if (!$success) {
    $errorMessage = error_get_last()['message'];die();
}else{
    echo "Message sent successfully...";
    }  
    break; 

    //-------------------------------------------------------------------------------------------------------

    case 'uploadPictures':  

if(isTheseParametersAvailable(array('id'))){ 
    $id = $_POST['id'];    
    $count = $_POST['count']; 
     $last = $_POST['last']; 
       $assign = $_POST['assign']; 
// print_r($_POST);die();
   
  
  for ($i=0; $i < $count; $i++)
{
      $counter = $i + 1;
    $file_path = "images/" . $id . "_" . $last . "_" . $counter. ".jpg";
     file_put_contents($file_path, base64_decode($_POST['picture'.$counter]));
   

    $stmt = $conn->prepare("INSERT INTO pictures (picture,userid,actid,assign_to) VALUES (?,?,?,?)");
    $stmt->bind_param("ssss", $file_path,$id,$last,$assign);  
    $stmt->execute();  

} 
    $stmt->close();   
  
    $response['error'] = false;   
    $response['message'] = 'pictures updated successfully';   
 
}
else
{  
    $response['error'] = true;   
    $response['message'] = 'required parameters are not available';   
}  

break; 
//------------------------------------------------------------------------------------------------//
case 'getpicturesAdmin':  
    //print_r($_POST);die();
    if(isTheseParametersAvailable(array('id'))){  
        $id = $_POST['id'];  
         $act_id = $_POST['act_id'];  

        $stmt = $conn->prepare("SELECT picture,id FROM pictures WHERE userid = ? AND actid = ?");  
        $stmt->bind_param("ss",$id,$act_id);  
        $stmt->execute(); 

            $stmt_result = $stmt->get_result();

            if($stmt_result->num_rows > 0){  
        $stmt->bind_result($picture,$id);  
        $activity = array();
        while($row_data = $stmt_result->fetch_assoc()){
         $temp = array();
         $temp['picture'] = UPLOADPATH.$row_data['picture']; 
         $temp['id'] = $row_data['id']; 

         array_push($activity, $temp);
     }
     $response['error'] = false;   
     $response['message'] = 'Images Fetch successfull';   
     $response['activity'] = $activity;   
 } 
}
        else{  
            $response['error'] = false;   
            $response['message'] = 'something wrong happened!';  
        }  
      
    break; 
//-----------------------------------------------------------------------------------------------------


case 'getpicturesExecutives':  
    //print_r($_POST);die();
    if(isTheseParametersAvailable(array('act_id'))){  
        $assign_to = $_POST['assign_to'];  
         $act_id = $_POST['act_id'];  

        $stmt = $conn->prepare("SELECT picture,id FROM pictures WHERE assign_to = ? AND actid = ?");  
        $stmt->bind_param("ss",$assign_to,$act_id);  
        $stmt->execute(); 

            $stmt_result = $stmt->get_result();

            if($stmt_result->num_rows > 0){  
        $stmt->bind_result($picture,$id);  
        $activity = array();
        while($row_data = $stmt_result->fetch_assoc()){
         $temp = array();
         $temp['picture'] = UPLOADPATH.$row_data['picture']; 
         $temp['id'] = $row_data['id']; 

         array_push($activity, $temp);
     }
     $response['error'] = false;   
     $response['message'] = 'Images Fetch successfull';   
     $response['activity'] = $activity;   
 } 
}
        else{  
            $response['error'] = false;   
            $response['message'] = 'something wrong happened!';  
        }  
      
    break; 
//-----------------------------------------------------------------------------------------------------

    case 'deleteImage':
if(isTheseParametersAvailable(array('id'))){   
    $id = $_POST['id']; 
    $stmt = $conn->prepare("DELETE from pictures where id = ?");  
    $stmt->bind_param("s",$id);  
    $stmt->execute();  
// $stmt->store_result(); 
    $response['error'] = false;   
    $response['message'] = 'Picture deleted successfull';   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'id not passed';  
}  
//}  
//}  
break; 
//--------------------------------------------------------------------------------------------------------------------------

case 'getAddedUsers':         

  //if(isTheseParametersAvailable(array('id'))){  
   //WHERE city_name = ?
$added_by = $_POST['added_by'];  

$stmt = $conn->prepare("SELECT fname,lname,email,phone,role FROM users where added_by = ? ");  
$stmt->bind_param("s",$added_by);  
$stmt->execute();  
$stmt_result = $stmt->get_result();
if($stmt_result->num_rows > 0){  
    $stmt->bind_result($fname,$lname,$email,$phone,$role);  
    $activity = array();
    while($row_data = $stmt_result->fetch_assoc()){
     $temp = array();
     $temp['fname'] = $row_data['fname']; 
     $temp['lname'] = $row_data['lname']; 
     $temp['email'] = $row_data['email']; 
     $temp['phone'] = $row_data['phone']; 
     $temp['role'] = $row_data['role']; 
    
     array_push($activity, $temp);

 }

 $response['error'] = false;   
 $response['message'] = 'Added Users Fetch successfull';   
 $response['activity'] = $activity;   
}  
else{  
    $response['error'] = false;   
    $response['message'] = 'Record not found';  
}  
//}  
break;   



//--------------------------------------------------------------------------------------------------------------------------------

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










