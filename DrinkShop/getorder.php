<?php
require_once 'db_functions.php';
$db = new DB_Functions();


if(isset($_POST['userPhone']) && isset($_POST['status']))
{
    $userPhone = $_POST['userPhone'];
    $status = $_POST['status'];

        $orders = $db->getOrderByStatus($userPhone,$status);
        
            echo json_encode($orders);
        
}

else{
    $response = "Required paramater (userPPhone,status) is missing!";
    echo json_encode($response);
}
?>