<?php
require('../connection.php');

$response = array();

// Query untuk mengambil data kuliner dari tabel
$command = "SELECT * FROM wisata";

// Mengeksekusi perintah select
$result = mysqli_query($connection, $command);

// Mengecek jumlah baris hasil query
$rows = mysqli_num_rows($result);

if ($rows > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data wisata tersedia";
    $response["dataWisata"] = array();

    // Menarik data dari database
    while ($row = mysqli_fetch_assoc($result)) {
        $temp["id_wisata"] = $row["id_wisata"];
        $temp["nama_wisata"] = $row["nama_wisata"];
        $temp["lokasi_wisata"] = $row["lokasi_wisata"];
        $temp["maps_wisata"] = $row["maps_wisata"];
        $temp["deskripsi_wisata"] = $row["deskripsi_wisata"];
        
        // Mengambil data gambar dari database
        $foto_wisata = $row["foto_wisata"];
        
        // Mengubah data gambar menjadi base64
        $base64_image = base64_encode($foto_wisata);
        
        // Menggabungkan data gambar dengan data lainnya
        $temp["foto_wisata"] = $base64_image;
        
        array_push($response["dataWisata"], $temp);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data wisata tidak tersedia";
}

// Mengubah menjadi format json
echo json_encode($response);

// Menutup koneksi
mysqli_close($connection);
?>
