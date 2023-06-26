<?php
     require('../connection.php');

     $response = array();

     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $id_wisata = $_POST["id_wisata"];

          $command = "DELETE FROM wisata WHERE id_wisata = '$id_wisata'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);

          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Data wisata berhasil didelete !";
          }
          else
          {
               $response["kode"] = 0;
               $response["pesan"] = "Data wisata gagal didelete !";
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