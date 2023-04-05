package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryResponse extends PagedResponse{

    private List<Status> stories;
    public StoryResponse(String message) {
        super(false,message, false);
    }

    public StoryResponse(List<Status> stories, boolean hasMorePages) {
        super(true, hasMorePages);
        this.stories = stories;
    }

    public List<Status> getStories()
    {
        return stories;
    }
    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        StoryResponse that = (StoryResponse) param;

        return (Objects.equals(stories, that.stories) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stories);
    }
}
