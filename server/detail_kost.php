<?php

$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// ckonekin ke db
$db = new DB_CONNECT();

$id_kost = $_GET['id_kost'];



$sql = "SELECT 	*  FROM kost WHERE id = '$id_kost' ";

	    $result = mysql_query($sql) or die(mysql_error());

		// cek
		
        if (mysql_num_rows($result) > 0) {
            $response["kost"] = array();

              

                while ($row = mysql_fetch_array($result)) {

                    $kost = array();
                
                  
                    $kost["id"] 		= $row["id"];
                    $kost["nama"] 		= $row["nama"];
                    $kost["alamat"] 	= $row["alamat"];
                    $kost["keterangan"] = $row["keterangan"];
                    $kost["gambar"] 	= $row["gambar"];
                    $kost["harga"] 	    = $row["harga"];
                    $kost["telepon"] 	    = $row["telepon"];
                    $kost["lat"] 	    = $row["latitude"];
                    $kost["log"] 	    = $row["logitude"];


                    array_push($response["kost"], $kost);
                    
                }

                $response["success"] = 1;
               	echo json_encode($response);            	
        }else{
            $response["success"] = 0;
		    $response["message"] = "Tidak ada data yang ditemukan";

		    echo json_encode($response);
        }







?>