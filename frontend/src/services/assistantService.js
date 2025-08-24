import apiClient from "./apiClient";

const assistantService = {
    sendMessage: async (question) => {
        return await apiClient.post('/assistant/chat', { question: question });
    }
};

export default assistantService;