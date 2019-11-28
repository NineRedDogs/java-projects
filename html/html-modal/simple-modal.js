//Get modal element
var modal = document.getElementById('simpleModal');
//Get modal button
var modalBtn = document.getElementById('modalBtn');
//Get close button
var closeBtn = document.getElementsByClassName('closeBtn')[0];


// Listen for open click
modalBtn.addEventListener('click', openModal);
// Listen for close click
closeBtn.addEventListener('click', closeModal);
// Listen for outside click
window.addEventListener('click', clickOutside);

// function to open modal
function openModal(){
    console.log("ajg pressed open button");
    console.log("modal ID : " + modal.id);
    console.log("modal ID : ", modal.id);
    modal.style.display = 'block';
}

// function to open modal
function closeModal(){
    console.log("ajg pressed closed button");
    modal.style.display = 'none';
}

// function to open modal
function clickOutside(e){
    console.log("ajg pressed click outside");
    if(e.target == modal) {
       modal.style.display = 'none';
    }
}
