package naturegecko.jingjok.configurations;

public class EnumConfig {

	public enum MINIO_DIRECTORY {

		USER_PROFILES("images/user_profiles/"), // User profiles will be kept in this directory.
		USER_BACKGROUNDS("images/user_backgrounds/"), // When users have an abilities to change their cover profile.
		MUSIC_THUMBNAILS("images/music_thumbnails/"), // Music thumbnail.
		MUSIC_BACKGROUNDS("images/music_backgrounds/"), // Musics also have background.
		PLAYLIST_THUMBNAILS("images/playlist_thumbnails/"), // Playlist thimbnail.
		PLAYLIST_BACKGROUNDS("images/playlist_backgrounds/"), // Playlist background.
		PLAYLIST_GALLERY("images/playlist_gallery/"), // A ton of images can be uploaded here.

		TRACKS_MUSICS("tracks/musics/"), // This is the place where tracks are kept.
		TRACKS_EFFECTS("tracks/effects/"); // Place where sound effect are stored.

		private final String destinationDirectory;

		private MINIO_DIRECTORY(String destinationDirectory) {
			this.destinationDirectory = destinationDirectory;
		}

		public String getDestinationDirectory() {
			return destinationDirectory;
		}
	}

	public enum IMAGE_PROFILE_CODE {
		USER_PROFILE(400), USER_BACKGROUND(1500), MUSIC_THUMBNAIL(400), MUSIC_BACKGROUND(1500);

		private final int profileWidth;

		private IMAGE_PROFILE_CODE(int profileWidth) {
			this.profileWidth = profileWidth;
		}

		public int getProfileWidth() {
			return profileWidth;
		}

	}

}
