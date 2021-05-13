// var loggedInResidentId;
// function initializeResidentAccountId(loggedInResidentId){
//     window.loggedInResidentId = loggedInResidentId;
// }
window.loggedInResidentId = null;

window.setResidentAccountId = {
    initializeResidentAccountId: function(loggedInResidentId_) {
        window.loggedInResidentId = loggedInResidentId_;
        // alert(loggedInResidentId_)
    }
}

window.LoggedInResidentAccountIdStore = {
    getLoggedInResidentAccountId: function() {
        // return this.$server.getLoggedInResidentId();
        return loggedInResidentId;
    }
}

// function window.samx() {
//         return "sam jack"
//     }


window.NS = {
    testJS: function (id)
    {
        console.log('NS::testJS - ' + id);
        alert('NS::testJS - ' + id);
    }
}