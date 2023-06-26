<?php
     // Memanggil koneksi.php
     require('../connection.php');

     $response = array();

     // mengecek data apakah ada atau tidak ada
     if($_SERVER["REQUEST_METHOD"] == "POST")
     {
          $nama_wisata = $_POST["nama_wisata"];
          $command = "SELECT * FROM wisata WHERE nama_wisata = '$nama_wisata'";

          $execute = mysqli_query($connection, $command);
          $check = mysqli_affected_rows($connection);
          
          if($check > 0)
          {
               $response["kode"] = 1;
               $response["pesan"] = "Data Berhasil ditemukan !";
               $response["dataScanWisata"] = array();
               
               while($get = mysqli_fetch_object($execute))
                {
                    $d["id_wisata"] = $get->id_wisata;  
                    $d["nama_wisata"] = $get->nama_wisata;
                    $d["lokasi_wisata"] = $get->lokasi_wisata;
                    $d["maps_wisata"] = $get->maps_wisata;
                    $d["deskripsi_wisata"] = $get->deskripsi_wisata;
                    
                    // Mendapatkan foto sebagai byte array
                    $foto_wisata_data = $get->foto_wisata;
                    // Mengubah byte array menjadi string base64
                    $foto_wisata_base64 = base64_encode($foto_wisata_data);
                    $d["foto_wisata"] = $foto_wisata_base64;
                    
                    array_push($response["dataScanWisata"], $d);
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
          $response["pesan"] = "Data Wisata tidak tersedia";
     }

     // Mengubah kedalam format json
     echo json_encode($response);

     // Menutup koneksi
     mysqli_close($connection);

?>