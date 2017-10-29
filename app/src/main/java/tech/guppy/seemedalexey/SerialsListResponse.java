package tech.guppy.seemedalexey;

import java.util.List;

/**
 * Created by serych on 19.10.2017.
 */

public class SerialsListResponse {
    private List<Serial> serials;

    public SerialsListResponse(List<Serial> serials) {
        this.serials = serials;
    }

    public List<Serial> getSerials() {
        return serials;
    }
}
