<?php
     // Memanggil koneksi.php
     require('../connection.php');

     $response = array();

     // mengecek data apakah ada atau tidak ada
     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $email = $_POST["email"];
          $password = $_POST["password"];
          $command = "SELECT * FROM user WHERE email = '$email' AND password = '$password'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);
          
          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Login Berhasil";
               $response["dataUser"] = array();
               
               while($get = mysqli_fetch_object($execute))
                {
                    $d["id_user"] = $get->id_user;  
                    $d["fullname"] = $get->fullname;
                    $d["email"] = $get->email;
                    $d["role"] = $get->role;
                    $d["phone"] = $get->phone;
                    
                    // Mendapatkan foto sebagai byte array
                    $foto_kuliner_data = $get->foto;
                    // Mengubah byte array menjadi string base64
                    $foto_kuliner_base64 = base64_encode($foto_kuliner_data);
                    $d["foto"] = $foto_kuliner_base64;
                    
                    $d["password"] = $get->password;
                    
                    array_push($response["dataUser"], $d);
                }
          }
          else
          {
               $response["kode"] = 0;
               $response["pesan"] = "Login Gagal";
          }

     }
     else
     {
          $response["kode"] = 0;
          $response["pesan"] = "Data user tidak tersedia";
     }

     // Mengubah kedalam format json
     echo json_encode($response);

     // Menutup koneksi
     mysqli_close($connection);

?>