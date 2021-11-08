
const xhr = new XMLHttpRequest();

xhr.open( "GET", "http://localhost:8080/api/profile?email=oladapoyuken59@gmail.com", true );

xhr.responseType = "arraybuffer";

xhr.onload = function( e ) {
    let arrayBufferView = new Uint8Array( this.response );
    let blob = new Blob( [ arrayBufferView ], { type: "image/jpeg" } );
    const urlCreator = window.URL || window.webkitURL;
    const imageUrl = urlCreator.createObjectURL( blob );
    let img = document.querySelector( "#myImage" );
    img.src = imageUrl;
};

xhr.send();