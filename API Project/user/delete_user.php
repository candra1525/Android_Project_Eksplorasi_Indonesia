<?php
     require('../connection.php');

     $response = array();

     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $id_user = $_POST["id_user"];

          $command = "DELETE FROM user WHERE id_user = '$id_user'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);

          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Data user berhasil didelete !";
          }
          else
          {
               $response["kode"] = 0;
               $response["pesan"] = "Data user gagal didelete !";
          }
          
     }
     else
     {
          $response["kode"] = 0;
          $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal didelete !";
     }

     echo json_encode($response);

     mysqli_close($connection);


?>