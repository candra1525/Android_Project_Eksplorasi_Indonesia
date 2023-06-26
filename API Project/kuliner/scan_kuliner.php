<?php
     // Memanggil koneksi.php
     require('../connection.php');

     $response = array();

     // mengecek data apakah ada atau tidak ada
     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $nama_kuliner = $_POST["nama_kuliner"];
          $command = "SELECT * FROM kuliner WHERE nama_kuliner = '$nama_kuliner'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);
          
          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Data Berhasil ditemukan !";
               $response["dataScanKuliner"] = array();
               
               while($get = mysqli_fetch_object($execute))
                {
                    $d["id_kuliner"] = $get->id_kuliner;  
                    $d["nama_kuliner"] = $get->nama_kuliner;
                    $d["asal_kuliner"] = $get->asal_kuliner;
                    $d["deskripsi_kuliner"] = $get->deskripsi_kuliner;
                    
                    // Mendapatkan foto sebagai byte array
                    $foto_kuliner_data = $get->foto_kuliner;
                    // Mengubah byte array menjadi string base64
                    $foto_kuliner_base64 = base64_encode($foto_kuliner_data);
                    $d["foto_kuliner"] = $foto_kuliner_base64;
                    
                    array_push($response["dataScanKuliner"], $d);
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
          $response["pesan"] = "Data Kuliner tidak tersedia";
     }

     // Mengubah kedalam format json
     echo json_encode($response);

     // Menutup koneksi
     mysqli_close($connection);

?>