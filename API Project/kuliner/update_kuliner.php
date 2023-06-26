<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id_kuliner = $_POST["id_kuliner"];
    $nama_kuliner = $_POST["nama_kuliner"];
    $asal_kuliner = $_POST["asal_kuliner"];
    $deskripsi_kuliner = $_POST["deskripsi_kuliner"];

    // Mengambil data gambar dari POST
    $foto_kuliner = $_FILES["foto_kuliner"]["tmp_name"];
    $foto_kuliner_name = $_FILES["foto_kuliner"]["name"];
    
     // Menghindari SQL injection
    $nama_kuliner = mysqli_real_escape_string($connection, $nama_kuliner);
    $asal_kuliner = mysqli_real_escape_string($connection, $asal_kuliner);
    $deskripsi_kuliner = mysqli_real_escape_string($connection, $deskripsi_kuliner);


    // Query untuk memperbarui data kuliner berdasarkan ID
    $command = "UPDATE kuliner SET nama_kuliner='$nama_kuliner', asal_kuliner='$asal_kuliner', foto_kuliner=?, deskripsi_kuliner='$deskripsi_kuliner' WHERE id_kuliner='$id_kuliner'";

    // Mempersiapkan pernyataan menggunakan prepared statement
    $stmt = mysqli_prepare($connection, $command);
    mysqli_stmt_bind_param($stmt, "b", $foto_kuliner_data);
    
    // Membaca dan mengunggah file gambar
    $foto_kuliner_data = file_get_contents($foto_kuliner);
    mysqli_stmt_send_long_data($stmt, 0, $foto_kuliner_data);
    
    mysqli_stmt_execute($stmt);

    $check = mysqli_stmt_affected_rows($stmt);

    if ($check > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data kuliner berhasil diupdate!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data kuliner gagal diupdate!";
    }

    mysqli_stmt_close($stmt);
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal diupdate";
}

echo json_encode($response);

mysqli_close($connection);
?>
