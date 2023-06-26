<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id_kuliner = $_POST["id_kuliner"];
    $nama_kuliner = $_POST["nama_kuliner"];
    $asal_kuliner = $_POST["asal_kuliner"];
    $deskripsi_kuliner = $_POST["deskripsi_kuliner"];
    
     // Menghindari SQL injection
    $nama_kuliner = mysqli_real_escape_string($connection, $nama_kuliner);
    $asal_kuliner = mysqli_real_escape_string($connection, $asal_kuliner);
    $deskripsi_kuliner = mysqli_real_escape_string($connection, $deskripsi_kuliner);


    // Query untuk memperbarui data kuliner berdasarkan ID
    $command = "UPDATE kuliner SET nama_kuliner='$nama_kuliner', asal_kuliner='$asal_kuliner', deskripsi_kuliner='$deskripsi_kuliner' WHERE id_kuliner='$id_kuliner'";

   // Mengeksekusi perintah SQL
    $result = mysqli_prepare($connection, $command);

    if ($result) {
        mysqli_stmt_execute($result);
        $response["kode"] = 1;
        $response["pesan"] = "Data kuliner berhasil diupdate!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data kuliner gagal diupdate!";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal diupdate";
}

echo json_encode($response);

mysqli_close($connection);
?>
