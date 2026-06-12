package moodify;

import java.util.ArrayList;
import java.util.List;

// ============================================================
//  DataManager.java  —  Loads all mood playlists (Singleton)
//  Demonstrates: static factory, data organisation
// ============================================================
public class DataManager {

    private static DataManager instance;
    private List<MoodPlaylist> allMoods;

    private DataManager() {
        allMoods = new ArrayList<>();
        loadAllPlaylists();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public List<MoodPlaylist> getAllMoods() { return allMoods; }

    public MoodPlaylist getMoodByName(String name) {
        for (MoodPlaylist m : allMoods)
            if (m.getName().equals(name)) return m;
        return null;
    }

    // -------------------------------------------------------
    private void loadAllPlaylists() {

        // =====================================================
        // 1. HAPPY MOOD — Muskurati Rahein
        // =====================================================
        MoodPlaylist happy = new MoodPlaylist("Happy Vibes", "☀️",
                " Enjoy your day!");
        happy.addSong("English", new Song("Happy","Pharrell Williams","audio/happy.wav",233,"English"));
        happy.addSong("English", new Song("Can't Stop the Feeling!","Justin Timberlake","audio/cantstop.wav",236,"English"));
        happy.addSong("English", new Song("Shake It Off","Taylor Swift","audio/shakeitoff.wav",219,"English"));
        happy.addSong("English", new Song("Walking on Sunshine","Katrina & The Waves","audio/sunshine.wav",238,"English"));
        happy.addSong("English", new Song("Good Time","Owl City & Carly Rae Jepsen","audio/goodtime.wav",205,"English"));
        happy.addSong("English", new Song("Firework","Katy Perry","audio/firework.wav",227,"English"));
        
        happy.addSong("Urdu/Hindi", new Song("Pasoori","Ali Sethi","audio/pasoori.wav",284,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Tu Jhoom","Abida Parveen, Naseebo Lal","audio/tujhoom.wav",310,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Gallan Goodiyaan","Shankar Mahadevan & Others","audio/gallangoodiyaan.wav",356,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Badtameez Dil","Benny Dayal","audio/badtameezdil.wav",252,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("London Thumakda","Neha Kakkar & Others","audio/londonthumakda.wav",230,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Kana Yaari","Kaifi Khalil","audio/kanayaari.wav",223,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Disco Deewane","Sunidhi Chauhan","audio/discodeewane.wav",221,"Urdu/Hindi"));
        happy.addSong("Urdu/Hindi", new Song("Abhi Toh Party Shuru Hui Hai","Badshah","audio/partyshuru.wav",179,"Urdu/Hindi"));
        allMoods.add(happy);

        // =====================================================
        // 2. SAD MOOD — Udas Lamhe
        // =====================================================
        MoodPlaylist sad = new MoodPlaylist("Feeling Low", "🖤",
                "It's okay to feel down sometimes.");
        sad.addSong("English", new Song("Before You Go","Lewis Capaldi","audio/before.wav",215,"English"));
        sad.addSong("English", new Song("Someone You Loved","Lewis Capaldi","audio/someoneyouloved.wav",182,"English"));
        sad.addSong("English", new Song("Another Love","Tom Odell","audio/anotherlove.wav",244,"English"));
        sad.addSong("English", new Song("The Night We Met","Lord Huron","audio/nightwemet.wav",208,"English"));
        sad.addSong("English", new Song("Fix You","Coldplay","audio/fixyou.wav",295,"English"));
        sad.addSong("Urdu/Hindi", new Song("Banjaara","Mohammed Irfan","audio/banjaara.wav",342,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Nadaan Parindey","Mohit Chauhan, A.R. Rahman","audio/nadaanparindey.wav",384,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Abhi Mujh Mein Kahin","Sonu Nigam","audio/abhimujhmein.wav",364,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Alag Aasmaan","Anuv Jain","audio/alagaasmaan.wav",215,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Bikhra","Abdul Hannan, Rovalio","audio/bikhra.wav",201,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Mehram","Asfar Hussain, Arooj Aftab","audio/mehram.wav",288,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Faaslay","Maanu","audio/faaslay.wav",176,"Urdu/Hindi"));
        sad.addSong("Urdu/Hindi", new Song("Jis Ko Jo Bhi Milta Hai","Asfer Hussain","audio/jiskojo.wav",340,"Urdu/Hindi"));
        allMoods.add(sad);

        // =====================================================
        // 3. MEDITATION — Sukoon-e-Dil
        // =====================================================
        MoodPlaylist meditation = new MoodPlaylist("Meditation Boost", "🧘",
                "Quiet your mind.");
        meditation.addSong("English", new Song("Weightless","Marconi Union","audio/weightless.wav",480,"English"));
        meditation.addSong("English", new Song("Watermark","Enya","audio/watermark.wav",145,"English"));
        meditation.addSong("English", new Song("An Ending (Ascent)","Brian Eno","audio/anending.wav",264,"English"));
        meditation.addSong("English", new Song("Sparks","Coldplay","audio/sparks.wav",227,"English"));
        meditation.addSong("English", new Song("Photograph","Ed Sheeran","audio/photograph.wav",258,"English"));
        
        meditation.addSong("Urdu/Hindi", new Song("Kun Faya Kun","A.R. Rahman","audio/kun_faya_kun.wav",470,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("Khamoshiyan","Arijit Singh","audio/khamoshiyan.wav",335,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("O Re Piya","Rahat Fateh Ali Khan","audio/orepiya.wav",379,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("Iktara","Kavita Seth","audio/iktara.wav",253,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("Tajdar-e-Haram","Atif Aslam","audio/tajdar_atif.wav",629,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("Alif Allah (Jugni)","Arif Lohar & Meesha Shafi","audio/alifallah.wav",318,"Urdu/Hindi"));
        meditation.addSong("Urdu/Hindi", new Song("Sham","Amit Trivedi, Nikhil D'Souza","audio/sham.wav",278,"Urdu/Hindi"));
        allMoods.add(meditation);

        // =====================================================
        // 4. MOTIVATION — Jazbaat-e-Zindagi
        // =====================================================
        MoodPlaylist motivation = new MoodPlaylist("Motivational Beats", "⚡",
                "Push boundaries.");
        motivation.addSong("English", new Song("Lose Yourself","Eminem","audio/lose.wav",326,"English"));
        motivation.addSong("English", new Song("Hall of Fame","The Script ft. Will.I.Am","audio/fame.wav",202,"English"));
        motivation.addSong("English", new Song("Eye of the Tiger","Survivor","audio/tiger.wav",245,"English"));
        motivation.addSong("English", new Song("Unstoppable","Sia","audio/unstoppable.wav",217,"English"));
        motivation.addSong("Urdu/Hindi", new Song("Yeh Honsla","Shafqat Amanat Ali","audio/yehhonsla.wav",283,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("Kar Har Maidaan Fateh","Sukhwinder Singh, Shreya Ghoshal","audio/maidaan.wav",311,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("Ye Dunya","Young Stunners ft. Khantrast","audio/yedunya.wav",245,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("Sinf-e-Aahan OST","Sinf-e-Aahan","audio/sinfeaahan.wav",210,"Urdu/Hindi"));

        motivation.addSong("Urdu/Hindi", new Song("Gumaan","Talha Anjum","audio/gumaan.wav",218,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("Afsanay","Young Stunners","audio/afsanay.wav",305,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("4AM in Karachi","Talha Anjum","audio/4amkarachi.wav",198,"Urdu/Hindi"));
        motivation.addSong("Urdu/Hindi", new Song("Agency","Young Stunners","audio/agency.wav",260,"Urdu/Hindi"));
        allMoods.add(motivation);

        // =====================================================
        // 5. HEARTBROKEN — Tute Hue Dil
        // =====================================================
        MoodPlaylist broken = new MoodPlaylist("Heart Broken", "💔",
                "Mending shattered emotions.");
        broken.addSong("English", new Song("Someone Like You","Adele","audio/someone.wav",285,"English"));
        broken.addSong("English", new Song("Before You Go","Lewis Capaldi","audio/before(2).wav",215,"English"));
        broken.addSong("English", new Song("Let Her Go","Passenger","audio/go.wav",252,"English"));
        broken.addSong("English", new Song("The Night We Met","Lord Huron","audio/nightwemet.wav",208,"English"));
        broken.addSong("English", new Song("All I Want","Kodaline","audio/alliwant.wav",305,"English"));
        broken.addSong("English", new Song("When I Was Your Man","Bruno Mars","audio/yourman.wav",213,"English"));
        broken.addSong("English", new Song("Too Good at Goodbyes","Sam Smith","audio/goodbyes.wav",201,"English"));
        
        broken.addSong("Urdu/Hindi", new Song("Khairiyat","Arijit Singh","audio/khairiyat.wav",240,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Bekhayali","Sachet Tandon","audio/bekhayali.wav",371,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Channa Mereya","Arijit Singh","audio/channa.wav",289,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Kahani Suno","Kaifi Khalil","audio/kahani.wav",173,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Tu","Talwiinder","audio/tu.wav",190,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Khayal","Talwiinder","audio/khayal.wav",185,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("O Bedardiya","Arijit Singh","audio/obedardiya.wav",313,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Bol Kaffara","Nusrat Fateh Ali Khan","audio/kaffara.wav",420,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Tarasti Hai Nigaahein","Asim Azhar","audio/taras.wav",240,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Ishqa Ve","Zeeshan Ali","audio/ishqa_ve.wav",230,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Tere Jeha","Talwiinder","audio/terejeha.wav",210,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Ye Jism Hai To Kya","Ali Azmat","audio/yejism.wav",231,"Urdu/Hindi"));

        broken.addSong("Urdu/Hindi", new Song("Tujhe Bhula Diya","Mohit Chauhan","audio/tujhebhula.wav",279,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Do Pal","Lata Mangeshkar, Sonu Nigam","audio/dopal.wav",246,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Ishq","Faheem Abdullah","audio/ishq.wav",250,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("O Sahib","Zain Zohaib and Adnan Dhool","audio/osahib.wav",210,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Beqarar Ye Dil","Asim Azhar","audio/beqarar.wav",195,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Ranjhna","Ali Sethi","audio/ranjhna.wav",230,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Tu Jo Nahin","Glenn John","audio/tujonahin.wav",290,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Musafir","Atif Aslam","audio/musafir.wav",265,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Mery Zindagi Hai Tu","Asim Azhar & Sabri Sisters","audio/meri_zindagi_asim.wav",240,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Mery Zindagi Hai Tu","Nusrat Fateh Ali Khan","audio/meri_zindagi_nfak.wav",480,"Urdu/Hindi"));
        broken.addSong("Urdu/Hindi", new Song("Aadat","Atif Aslam","audio/aadat.wav",328,"Urdu/Hindi"));

        allMoods.add(broken);

        // =====================================================
        // 6. STARRY NIGHTS — Khwabon Ki Raat
        // =====================================================
        MoodPlaylist night = new MoodPlaylist("Starry Nights", "🌌",
                "Cosmic dynamic late night synth waves.");
        night.addSong("English", new Song("Night Changes","One Direction","audio/night_changes.wav",226,"English"));
        night.addSong("English", new Song("Until I Found You","Stephen Sanchez","audio/untilifoundyou.wav",177,"English"));
        night.addSong("English", new Song("Sweater Weather","The Neighbourhood","audio/sweater.wav",240,"English"));
        night.addSong("English", new Song("The Night We Met","Lord Huron","audio/nightwemet.wav",208,"English"));
        night.addSong("English", new Song("Sparks","Coldplay","audio/sparks.wav",227,"English"));
        night.addSong("English", new Song("Photograph","Ed Sheeran","audio/photograph.wav",258,"English"));
        night.addSong("English", new Song("Yellow","Coldplay","audio/yellow_night.wav",269,"English"));
        
        night.addSong("Urdu/Hindi", new Song("Falak Tak Chal","Udit Narayan, Mahalaxmi Iyer","audio/falaktak.wav",306,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Agar Tum Saath Ho","Alka Yagnik, Arijit Singh","audio/agar_tum_saath.wav",341,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Ye Tune Kya Kiya","Javed Bashir","audio/yetunekyakiya.wav",315,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Hasi (Female Version)","Shreya Ghoshal","audio/hasi.wav",272,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Sahiba","Bilal Saeed","audio/sahiba.wav",220,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Kabira (Reprise)","Arijit Singh, Harshdeep Kaur","audio/kabira.wav",268,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Aisay Kaisay","Hassan Raheem","audio/aisaykaisay.wav",162,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Joona","Hassan Raheem","audio/joona.wav",155,"Urdu/Hindi"));
        night.addSong("Urdu/Hindi", new Song("Maand","Hassan Raheem","audio/maand.wav",170,"Urdu/Hindi"));
        allMoods.add(night);

        // =====================================================
        // 7. PUNJABI — Punjabi Masti
        // =====================================================
        MoodPlaylist punjabi = new MoodPlaylist("Punjabi Masti", "🕺",
                "Pure energetic Desi beats.");
        punjabi.addSong("All", new Song("Brown Munde","AP Dhillon, Gurinder Gill, Shinda Kahlon","audio/brown_munde.wav",268,"All"));
        punjabi.addSong("All", new Song("High Rated Gabru","Guru Randhawa","audio/gabru.wav",214,"All"));
        punjabi.addSong("All", new Song("GOAT","Diljit Dosanjh","audio/goat.wav",223,"All"));
        punjabi.addSong("All", new Song("We Rollin","Shubh","audio/werollin.wav",199,"All"));

        punjabi.addSong("All", new Song("Excuses","AP Dhillon, Gurinder Gill","audio/excuses.wav",176,"All"));
        punjabi.addSong("All", new Song("With You","AP Dhillon","audio/withyou.wav",156,"All"));
        punjabi.addSong("All", new Song("Softly","Karan Aujla","audio/softly.wav",201,"All"));
        punjabi.addSong("All", new Song("Admirin' You","Karan Aujla, Preston Pablo","audio/admiringyou.wav",212,"All"));
        punjabi.addSong("All", new Song("Lover","Diljit Dosanjh","audio/lover.wav",191,"All"));
        punjabi.addSong("All", new Song("Born To Shine","Diljit Dosanjh","audio/bornshine.wav",213,"All"));
        punjabi.addSong("All", new Song("Hass Hass","Diljit Dosanjh, Sia","audio/hasshass.wav",160,"All"));
        punjabi.addSong("All", new Song("Obsessed","Riar Saab, Abhijay Sharma","audio/obsessed.wav",192,"All"));
        punjabi.addSong("All", new Song("Elevated","Shubh","audio/elevated.wav",202,"All"));
        punjabi.addSong("All", new Song("Winning Speech","Karan Aujla","audio/winningspeech.wav",220,"All"));
        punjabi.addSong("All", new Song("Naah","Harrdy Sandhu","audio/naah.wav",195,"All"));
        allMoods.add(punjabi);

        // =====================================================
        // 8. QAWWALI — Qawwali Night
        // =====================================================
        MoodPlaylist qawwali = new MoodPlaylist("Suroor e Qawwali", "🕌",
                "Ethereal mystical sounds.");
        qawwali.addSong("All", new Song("Tajdar-e-Haram","Atif Aslam / Sabri Brothers","audio/tajdar_atif.wav",629,"All"));
        qawwali.addSong("All", new Song("Dil Pe Zakhm Khate Hai","Nusrat Fateh Ali Khan","audio/dilpezakhm.wav",510,"All"));
        qawwali.addSong("All", new Song("Sanso Ki Maala","Nusrat Fateh Ali Khan","audio/saansoki.wav",600,"All"));
        qawwali.addSong("All", new Song("Kali Kali Zulfo k Phande na dalo","Nusrat Fateh Ali Khan","audio/kalikali.wav",495,"All"));
        qawwali.addSong("All", new Song("Aj Koi Baat Ho Gai","Nusrat Fateh Ali Khan","audio/aajkoibaat.wav",430,"All"));
        qawwali.addSong("All", new Song("Un K Andaaz e Karam","Nusrat Fateh Ali Khan","audio/andaaz.wav",460,"All"));
        qawwali.addSong("All", new Song("Dekhte Dekhte","Nusrat Fateh Ali Khan","audio/dekhtedekhte.wav",420,"All"));

        qawwali.addSong("All", new Song("Mere Rashke Qamar","Nusrat Fateh Ali Khan","audio/rashke_qamar.wav",410,"All"));
        qawwali.addSong("All", new Song("Tumhi Dil Lagi","Nusrat Fateh Ali Khan","audio/dillagi.wav",540,"All"));
        qawwali.addSong("All", new Song("Tu Kuja Man Kuja","Rafaqat Ali Khan","audio/tukuja.wav",480,"All"));
        qawwali.addSong("All", new Song("Chap Tilak","Abida Parveen","audio/chaptilak.wav",390,"All"));

        qawwali.addSong("All", new Song("Allah Hu","Nusrat Fateh Ali Khan","audio/allahhu.wav",680,"All"));
        qawwali.addSong("All", new Song("Afreen Afreen","Nusrat Fateh Ali Khan","audio/afreen.wav",582,"All"));
        qawwali.addSong("All", new Song("Dum Mast Qalandar","Abida Parveen","audio/dummast.wav",410,"All"));
        qawwali.addSong("All", new Song("Bhar Do Jholi Meri","Sabri Brothers","audio/bhardoijholi.wav",650,"All"));
        qawwali.addSong("All", new Song("Man Kunto Maula","Rahat Fateh Ali Khan","audio/mankunto.wav",380,"All"));
        qawwali.addSong("All", new Song("Balaghal Ula Be Kamalehi","Nusrat Fateh Ali Khan","audio/balaghal.wav",720,"All"));
        qawwali.addSong("All", new Song("Ali Maula Ali Maula","Various Artists","audio/alimaula.wav",310,"All"));
        qawwali.addSong("All", new Song("Yeh Jo Halka Halka Suroor Hai","Nusrat Fateh Ali Khan","audio/suroor.wav",740,"All"));
        allMoods.add(qawwali);

        // =====================================================
        // 9. RAINY — Barsaati Melodies
        // =====================================================
        MoodPlaylist rainy = new MoodPlaylist("Barsaati Melodies", "🌧️",
                "Gentle drops, soft music.");
        rainy.addSong("English", new Song("Sweater Weather","The Neighbourhood","audio/sweater.wav",240,"English"));
        rainy.addSong("English", new Song("Someone Like You (rainy vibe)","Adele","audio/someone.wav",290,"English"));
        rainy.addSong("English", new Song("Yellow","Coldplay","audio/yellow.wav",269,"English"));

        rainy.addSong("English", new Song("Set Fire to the Rain","Adele","audio/set_fire_rain.wav",242,"English"));
        rainy.addSong("English", new Song("Rain On Me","Lady Gaga, Ariana Grande","audio/rainonme.wav",182,"English"));
        rainy.addSong("English", new Song("Let It Rain","Ed Sheeran","audio/letitrain.wav",210,"English"));
        rainy.addSong("English", new Song("When the Rain Stops","Dua Lipa","audio/whenrainstops.wav",198,"English"));
        
        rainy.addSong("Urdu/Hindi", new Song("Baarishein","Anuv Jain","audio/baarishein.wav",207,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Kabira (Reprise)","Arijit Singh, Harshdeep Kaur","audio/kabira.wav",268,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Kabhi Jo Badal Barse","Arijit Singh","audio/kabhijobaadal.wav",254,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Tum Se Hi","Mohit Chauhan","audio/tumsehi.wav",321,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Baarish","Ash King, Shashaa Tirupati","audio/baarish.wav",275,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Kaisay hua","Full Forms","audio/kaisayhua.wav",240,"Urdu/Hindi"));
        rainy.addSong("Urdu/Hindi", new Song("Pehli Baarish","Ali Zafar","audio/pehlibaarish.wav",225,"Urdu/Hindi"));

        allMoods.add(rainy);
    }
}
