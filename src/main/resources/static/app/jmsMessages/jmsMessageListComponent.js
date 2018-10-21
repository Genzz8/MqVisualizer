angular.
  module('jmsMessageApp').
  component('messageList', {
        templateUrl: '../app/jmsMessages/messageListTemplate.html',
        controller: function JmsMessagesListController(DetailService, $stateParams, $state) {
            var self = this;
            self.messages = $stateParams.data;
            self.message = {};
            
            
            self.getMessage = getMessage;
            
            
            function getMessage(index) {
            	var message = self.messages[index];
            	DetailService.getMessage(
                		message, 
                		function(res){
                			var message = res.data;
                			$state.go('detail', {data:message});
                		}, function(error) {
                			console.log(error);
                		})
                
            }
            
            
            
        }
    });