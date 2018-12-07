<?php
require_once 'db_functions.php';
$db = new DB_Functions();

/*
http://localhost/drinkshop/submitorder.php
 */

$response = array();
if(isset($_POST['orderDetail']) &&
    isset($_POST['phone']) &&
    isset($_POST['address']) &&
    isset($_POST['comment']) &&
    isset($_POST['price']) &&
    isset($_POST['paymentMethod']))
    {
    $phone = $_POST['phone'];
    $orderDetail = $_POST['orderDetail'];
    $orderAddress = $_POST['address'];
    $orderComment = $_POST['comment'];
    $orderPrice = $_POST['price'];
    $paymentMethod = $_POST['paymentMethod'];

    $result = $db->insertNewOrder($orderPrice,$orderComment,$orderAddress,$orderDetail,$phone,$paymentMethod);
    if($result)
        echo json_encode("true");
    else
        echo json_encode($this->conn->error);

}
else{
    echo json_encode("Required paramater (phone,detail,address,comment,price,paymentMethod) is missing!");
}
?>