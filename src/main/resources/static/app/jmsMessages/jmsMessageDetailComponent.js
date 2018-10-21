angular.
  module('jmsMessageApp').
  component('messageDetail', {
        templateUrl: '../app/jmsMessages/messageDetailTemplate.html',
        controller: function JmsMessagesDetailController( DetailService, Messages, $stateParams, $state, $rootScope ) {
            var self = this;
            self.message = $stateParams.data;

            self.deleteMsg = deleteMessage;
            
            function deleteMessage() {
            	
            	DetailService.deleteMessage(
            		self.message, 
            		function(res){
            			getList();
            		}, function(error) {
            			console.log(error);
            		}
            	)
            	
            }
            
            $rootScope.$on('$locationChangeStart', function(event, toUrl, fromUrl) {
            	if(toUrl.endsWith("jmsList")){
            		event.preventDefault();
            		getList();
            	}
            	
            });

        	$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        		console.log(toUrl);
        	});
            
        	function getList() {
                
                Messages.getMessages({ 
                    paramQueue:self.message.paramQueue,
                    dataQueue:self.message.dataQueue,
                }, function(res){
                    var messages = res.data;
                    if(messages.length <= 0)
                    	return
                    $state.go('jmsList', {data:messages});
                }, function(error) {
                   console.log(error);
                  });
            }
        }
  
    });