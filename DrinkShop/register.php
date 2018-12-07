<?php
require_once 'db_functions.php';
$db = new DB_Functions();

/*
http://localhost/drinkshop/register.php/?phone=09091111&name=Hung&birthdate=1997-2-12&address=121DN
 */

$response = array();
if(isset($_POST['phone']) &&
    isset($_POST['name']) &&
    isset($_POST['birthdate']) &&
    isset($_POST['address']))
    {
    $phone = $_POST['phone'];
    $name = $_POST['name'];
    $birthdate = $_POST['birthdate'];
    $address = $_POST['address'];

    if($db->checkUserExists($phone)){
        $response ["error_msg"] = "User already existed with ".$phone;
        echo json_encode($response);
    }
    else{
        //check new user
        $user = $db->registerNewUser($phone,$name,$birthdate,$address);
        if($user){
            $response ["phone"] = $user["Phone"];
            $response ["name"] = $user["Name"];
            $response ["birthdate"] = $user["Birthdate"];
            $response ["address"] = $user["Address"];
            echo json_encode($response);
        }
        else{
            $response ["error_msg"] = "Unknow error occurred in registration!";
            echo json_encode($response);
        }
    }
}
else{
    $response ["error_msg"] = "Required paramater (phone,name,birthdate,address) is missing!";
    echo json_encode($response);
}
?>