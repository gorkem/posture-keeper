
package com.redhat.postureKeeper.sarif;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * Key/value pairs that provide additional information about the object.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tags"
})
@Generated("jsonschema2pojo")
public class PropertyBag {

    /**
     * A set of distinct strings that provide additional information.
     * 
     */
    @JsonProperty("tags")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("A set of distinct strings that provide additional information.")
    private Set<String> tags = new LinkedHashSet<String>();

    /**
     * A set of distinct strings that provide additional information.
     * 
     */
    @JsonProperty("tags")
    public Set<String> getTags() {
        return tags;
    }

    /**
     * A set of distinct strings that provide additional information.
     * 
     */
    @JsonProperty("tags")
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PropertyBag.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("tags");
        sb.append('=');
        sb.append(((this.tags == null)?"<null>":this.tags));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.tags == null)? 0 :this.tags.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PropertyBag) == false) {
            return false;
        }
        PropertyBag rhs = ((PropertyBag) other);
        return ((this.tags == rhs.tags)||((this.tags!= null)&&this.tags.equals(rhs.tags)));
    }

}
