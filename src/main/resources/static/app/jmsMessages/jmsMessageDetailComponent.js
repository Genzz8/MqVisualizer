angular.
  module('jmsMessageApp').
  component('messageDetail', {
        templateUrl: '../app/jmsMessages/messageDetailTemplate.html',
        controller: function JmsMessagesDetailController( DetailService, $stateParams, $state ) {
            var self = this;
            self.message = $stateParams.data;

            self.deleteMsg = deleteMessage;
            
            function deleteMessage() {
            	
            	debugger;
            	DetailService.deleteMessage(
            		self.message, 
            		function(res){
            			$state.go('queueList');
            		}, function(error) {
            			console.log(error);
            		}
            	)
            	
            	
            }
            
        }
  
    });