<?php
require('../connection.php');

$response = array();

// Query untuk mengambil data kuliner dari tabel
$command = "SELECT * FROM user";

// Mengeksekusi perintah select
$result = mysqli_query($connection, $command);

// Mengecek jumlah baris hasil query
$rows = mysqli_num_rows($result);

if ($rows > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data user tersedia";
    $response["dataUser"] = array();

    // Menarik data dari database
    while ($row = mysqli_fetch_assoc($result)) {
        $temp["id_user"] = $row["id_user"];
        $temp["fullname"] = $row["fullname"];
        $temp["email"] = $row["email"];
        $temp["role"] = $row["role"];
        $temp["phone"] = $row["phone"];
        $temp["password"] = $row["password"];
        
        // Mengambil data gambar dari database
        $foto = $row["foto"];
        
        // Mengubah data gambar menjadi base64
        $base64_image = base64_encode($foto);
        
        // Menggabungkan data gambar dengan data lainnya
        $temp["foto"] = $base64_image;
        
        array_push($response["dataUser"], $temp);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data User tidak tersedia";
}

// Mengubah menjadi format json
echo json_encode($response);

// Menutup koneksi
mysqli_close($connection);
?>
