package com.sunnepah.savewithme;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import com.meltmedia.dropwizard.mongo.MongoConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveWithMeConfiguration extends Configuration {
	
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
    @Valid
    @NotNull
    @JsonProperty
    private ClientSecretsConfiguration clientSecrets = new ClientSecretsConfiguration();

	@Valid
	@NotNull
	@JsonProperty
	private OAuth oauth;

	@JsonProperty
    protected MongoConfiguration mongo;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

	public JerseyClientConfiguration getJerseyClient() {
        return httpClient;
    }
	
	public ClientSecretsConfiguration getClientSecrets() {
		return clientSecrets;
	}

	public OAuth getOauth() {
		return oauth;
	}

    public MongoConfiguration getMongo() {
        return mongo;
    }
	
	public static class ClientSecretsConfiguration {
		
		@NotBlank
		@JsonProperty
		String facebook;
		
		@NotBlank
		@JsonProperty
		String google;
		
		@NotBlank
		@JsonProperty
		String linkedin;
		
		@NotBlank
		@JsonProperty
		String github;
		
		@NotBlank
		@JsonProperty
		String foursquare;
		
		@NotBlank
		@JsonProperty
		String twitter;
		
		public String getFacebook() {
			return facebook;
		}

		public String getGoogle() {
			return google;
		}
		
		public String getLinkedin() {
			return linkedin;
		}
		
		public String getFoursquare() {
			return foursquare;
		}
		
		public String getTwitter() {
			return twitter;
		}
	}

	public static class OAuth {
		public Facebook facebook;

		public Google google;
	}

	public static class Facebook {
		@NotBlank
		@JsonProperty
		String accessTokenUrl;

		@NotBlank
		@JsonProperty
		String graphApiUrl;

		public String getGraphApiUrl() {
			return graphApiUrl;
		}

		public String getAccessTokenUrl() {
			return accessTokenUrl;
		}
	}

	public static class Google {
		@NotBlank
		@JsonProperty
		String accessTokenUrl;

		@NotBlank
		@JsonProperty
		String peopleApiUrl;

		public String getAccessTokenUrl() {
			return accessTokenUrl;
		}

		public String getPeopleApiUrl() {
			return peopleApiUrl;
		}
	}
}
