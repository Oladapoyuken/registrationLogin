$(document).ready(function() {
    $("input").prop("disabled", "true");
    $("select").prop("disabled", "true");
    $("#imageUpload").removeAttr("disabled");
    const email = localStorage.getItem("email");
    const token = "Bearer " + localStorage.getItem("token");

    axios.get(
        'http://localhost:8080/api/user?email='+ email,
        {
            headers:{
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }

    ).then((response) => {
        console.log(response);
        if(response.data.status == "expired"){
            window.location.assign("login.html");
        }
        else if(response.data.status == "failure"){
            alert(response.data.message);
        }
        else{
            const data = response.data;
            $('#firstName').val(data.firstName);
            $('#lastName').val(data.lastName);
            $('#gender').val(data.gender);
            $('#age').val(data.age);
            $('#email').val(data.email);
            $('#address').val(data.address);
            $('#phoneNumber').val(data.phoneNumber);
            $('#occupation').val(data.occupation);
        }

    }).catch(err => {
        console.log(err);
    });

    async function getPicture(){
        try{
            const response = await axios.get(
                'http://localhost:8080/api/profile?email='+ email,
                {
                    headers:{
                        'Content-Type': 'application/json',
                        'Authorization': token
                    },
                    responseType: 'arraybuffer'
                }

            );
            console.log(response);

            let arrayBufferView = new Uint8Array( response.data );
            let blob = new Blob( [ arrayBufferView ], { type: "image/jpeg" } );
            const urlCreator = window.URL || window.webkitURL;
            const imageUrl = urlCreator.createObjectURL( blob );
            let img = document.querySelector( "#picture" );
            img.src = imageUrl;
        }
        catch (error){
            console.log(error);
        }
    }

    getPicture();

    // axios.get(
    //     'http://localhost:8080/api/profile?email='+ email,
    //     {
    //         headers:{
    //             'Content-Type': 'application/json',
    //             'Authorization': token
    //         },
    //         responseType: 'arraybuffer'
    //     }
    //
    // ).then((response) => {
    //     console.log(response);
    //     let arrayBufferView = new Uint8Array( response.data );
    //     let blob = new Blob( [ arrayBufferView ], { type: "image/jpeg" } );
    //     const urlCreator = window.URL || window.webkitURL;
    //     const imageUrl = urlCreator.createObjectURL( blob );
    //     let img = document.querySelector( "#picture" );
    //     img.src = imageUrl;
    //
    // }).catch(err => {
    //     console.log(err);
    // })

    // let xhr = new XMLHttpRequest();
    // xhr.open( "GET", "http://localhost:8080/api/profile?email="+email, true );
    // xhr.setRequestHeader("Authorization", token);
    //
    // xhr.responseType = "arraybuffer";
    //
    // xhr.onload = function( e ) {
    //     let arrayBufferView = new Uint8Array( this.response );
    //     let blob = new Blob( [ arrayBufferView ], { type: "image/jpeg" } );
    //     const urlCreator = window.URL || window.webkitURL;
    //     const imageUrl = urlCreator.createObjectURL( blob );
    //     let img = document.querySelector( "#picture" );
    //     img.src = imageUrl;
    // };
    //
    // xhr.send();
});

$("#btn").click(function (){
    const token = "Bearer " + localStorage.getItem("token");
    const email = localStorage.getItem("email");
    let val = $("#btn").html();
    if(val == "Edit"){
        $("input").removeAttr("disabled");
        $("#imageUpload").removeAttr("disabled");
        $("select").removeAttr("disabled");
        $("#email").prop("disabled", "true");
        $("#btn").html("Update");
    }
    else{
        const firstname = $('#firstName').val();
        const lastname = $('#lastName').val();
        const occupation = $('#occupation').val();
        const gender = $('#gender').val();
        const phone = $('#phoneNumber').val();
        const age = $('#age').val();
        const address = $('#address').val();

        if(firstname == '' || lastname == '' || phone == '' || occupation == '' || age == '' || address == ''){
            alert("Please fill in all inputs");
        }
        else{
            const user = {
                firstName : firstname,
                lastName : lastname,
                address : address,
                gender : gender,
                phoneNumber: phone,
                occupation : occupation,
                age : age,
                email: email
            };

            axios.put(
                'http://localhost:8080/api/update',
                user,
                {
                    headers:{
                        'Content-Type': 'application/json',
                        'Authorization': token
                    }
                }

            ).then((response) =>{
                console.log(response);
                if(response.data.status == "success"){
                    $("#btn").html("Edit");
                    $("input").prop("disabled", "true");
                    $("select").prop("disabled", "true");
                    $("#imageUpload").removeAttr("disabled");
                    alert(response.data.message);

                }

            }).catch(err => {
                console.log(err);
            });
        }
    }
});


$("#imageUpload").change(function () {
    const input = $("#imageUpload").prop("files")[0];
    console.log(input);
    if (input) {
        let reader = new FileReader();
        reader.onload = function (e) {
            let img = document.querySelector( "#picture" );
            img.src = e.target.result;
        };
        reader.readAsDataURL(input);

        const email = localStorage.getItem("email");
        const token = "Bearer " + localStorage.getItem("token");

        const formData = new FormData();
        formData.append("file", input);
        formData.append("email", email);

        axios.post(
            'http://localhost:8080/api/upload',
            formData,
            {
                headers:{
                    'Content-Type': 'multipart/form-data',
                    'Authorization': token
                }
            }
        ).then((response) => {
            alert(response.data.message);
            console.log(response);
        }).catch(err => {
            console.log(err);
        });

    }
});

$("#logout").click(() => {
   localStorage.clear();
});