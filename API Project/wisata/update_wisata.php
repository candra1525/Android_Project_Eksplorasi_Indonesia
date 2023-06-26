<?php
require('../connection.php');

$response = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id_wisata = $_POST["id_wisata"];
    $nama_wisata = $_POST["nama_wisata"];
    $lokasi_wisata = $_POST["lokasi_wisata"];
    $maps_wisata = $_POST["maps_wisata"];
    $deskripsi_wisata = $_POST["deskripsi_wisata"];

    // Mengambil data gambar dari POST
    $foto_wisata = $_FILES["foto_wisata"]["tmp_name"];
    $foto_wisata_name = $_FILES["foto_wisata"]["name"];
    
    // Menghindari SQL injection
    $nama_wisata = mysqli_real_escape_string($connection, $nama_wisata);
    $lokasi_wisata = mysqli_real_escape_string($connection, $lokasi_wisata);
    $maps_wisata = mysqli_real_escape_string($connection, $maps_wisata);
    $deskripsi_wisata = mysqli_real_escape_string($connection, $deskripsi_wisata);


    // Query untuk memperbarui data kuliner berdasarkan ID
    $command = "UPDATE wisata SET nama_wisata='$nama_wisata', lokasi_wisata='$lokasi_wisata', maps_wisata='$maps_wisata', foto_wisata=?, deskripsi_wisata='$deskripsi_wisata' WHERE id_wisata='$id_wisata'";

    // Mempersiapkan pernyataan menggunakan prepared statement
    $stmt = mysqli_prepare($connection, $command);
    mysqli_stmt_bind_param($stmt, "b", $foto_wisata_data);
    
    // Membaca dan mengunggah file gambar
    $foto_wisata_data = file_get_contents($foto_wisata);
    mysqli_stmt_send_long_data($stmt, 0, $foto_wisata_data);
    
    mysqli_stmt_execute($stmt);

    $check = mysqli_stmt_affected_rows($stmt);

    if ($check > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data wisata berhasil diupdate!";
    } else {
        $response["kode"] = 0;
        $response["pesan"] = "Data wisata gagal diupdate!";
    }

    mysqli_stmt_close($stmt);
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada data yang berhasil ataupun gagal diupdate";
}

echo json_encode($response);

mysqli_close($connection);
?>
