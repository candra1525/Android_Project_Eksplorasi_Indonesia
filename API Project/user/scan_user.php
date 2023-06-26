<?php
     // Memanggil koneksi.php
     require('../connection.php');

     $response = array();

     // mengecek data apakah ada atau tidak ada
     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $fullname = $_POST["fullname"];
          $command = "SELECT * FROM user WHERE fullname = '$fullname'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);
          
          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Data Berhasil ditemukan !";
               $response["dataScanUser"] = array();
               
               while($get = mysqli_fetch_object($execute))
                {
                    $d["id_user"] = $get->id_user;  
                    $d["fullname"] = $get->fullname;
                    $d["email"] = $get->email;
                    $d["role"] = $get->role;
                    $d["phone"] = $get->phone;
                    $d["password"] = $get->password;
                    
                    // Mendapatkan foto sebagai byte array
                    $foto_user_data = $get->foto;
                    // Mengubah byte array menjadi string base64
                    $foto_user_base64 = base64_encode($foto_user_data);
                    $d["foto"] = $foto_user_base64;
                    
                    array_push($response["dataScanUser"], $d);
                }
          }
          else
          {
               $response["kode"] = 0;
               $response["pesan"] = "Data Gagal ditemukan !";
          }

     }
     else
     {
          $response["kode"] = 0;
          $response["pesan"] = "Data User tidak tersedia";
     }

     // Mengubah kedalam format json
     echo json_encode($response);

     // Menutup koneksi
     mysqli_close($connection);

?>