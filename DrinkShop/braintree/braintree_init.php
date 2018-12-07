<?php

session_start();
require_once ("lib/autoload.php");

if(file_exists(__DIR__ . "/../.env"))
{
    $dotenv = new Dotenv\Dotenv(__DIR__ . "/../");
    $dotenv->load();
}

Braintree_Configuration::environment('sandbox');
Braintree_Configuration::merchantId('npmxywkhqz5srqzc');
Braintree_Configuration::publicKey('zxbkzh3rkqfkcrnv');
Braintree_Configuration::privateKey('80ab1c5a085a2955988d662ea92af605');

?>