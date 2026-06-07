package io.github.amsatrio.spring_crud_demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Generator {
    private static final String WILAYAH_FILE_PATH = "/home/mos/drive_0/workspace/spring/spring_hospital/db/wilayah.sql";

    private static final String[] DOMAINS = {
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com", "icloud.com", "protonmail.com",
            "zoho.com",
            "mail.com", "gmx.com", "yandex.com", "inbox.com", "live.com", "me.com", "fastmail.com", "tutanota.com",
            "mailinator.com", "example.com", "testmail.com", "dummyemail.com", "example.org", "domain.com", "mail.ru",
            "hotmail.co.uk", "qq.com", "163.com", "outlook.com.au", "rocketmail.com", "email.com", "rediffmail.com",
            "mail2world.com", "cox.net", "att.net", "comcast.net", "sbcglobal.net", "verizon.net", "ntlworld.com",
            "blueyonder.co.uk", "talktalk.net", "orange.fr", "gmx.net", "web.de", "t-online.de", "freenet.de",
            "arcor.de",
            "yahoo.co.jp", "yahoo.co.uk", "yahoo.co.in", "yahoo.ca", "yahoo.com.au", "yahoo.fr", "yahoo.de", "yahoo.it",
            "yahoo.es", "163.net", "126.com", "yeah.net", "foxmail.com", "aliyun.com", "sina.com", "sina.cn",
            "sohu.com",
            "tom.com", "126.net", "139.com", "189.cn", "21cn.com", "qq.com", "139.com", "163.com", "vip.qq.com",
            "vip.sina.com", "vip.163.com", "vip.sohu.com", "vip.126.com", "vip.qq.com", "163.net", "126.net", "qq.com",
            "yahoo.co.jp", "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com", "icloud.com",
            "protonmail.com",
            "zoho.com", "mail.com", "gmx.com", "yandex.com", "inbox.com", "live.com", "me.com", "fastmail.com",
            "tutanota.com"
    };
    private static final String[] FIRST_NAMES = {
            "Liam", "Olivia", "Noah", "Emma", "Oliver", "Ava", "Elijah", "Charlotte", "William", "Sophia",
            "James", "Amelia", "Benjamin", "Isabella", "Lucas", "Mia", "Henry", "Evelyn", "Alexander", "Harper",
            "Michael", "Abigail", "Ethan", "Emily", "Daniel", "Elizabeth", "Matthew", "Ella", "Jackson", "Avery",
            "Sebastian", "Sofia", "David", "Camila", "Carter", "Scarlett", "Wyatt", "Madison", "Jayden", "Luna",
            "John", "Grace", "Owen", "Chloe", "Dylan", "Penelope", "Luke", "Layla", "Gabriel", "Riley",
            "Anthony", "Zoey", "Isaac", "Nora", "Grayson", "Lily", "Christopher", "Mila", "Joshua", "Aria",
            "Andrew", "Eleanor", "Theodore", "Hannah", "Caleb", "Addison", "Nathan", "Ellie", "Ryan", "Natalie",
            "Jack", "Lillian", "Samuel", "Violet", "Joseph", "Stella", "Levi", "Aurora", "Mateo", "Savannah",
            "Lincoln", "Claire", "Luke", "Lucy", "Hunter", "Anna", "Christian", "Leah", "Jaxon", "Katherine",
            "Isaiah", "Sarah", "Eli", "Aaliyah", "Aaron", "Gabriella", "Charles", "Sadie", "Connor", "Aubrey"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Hall", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
            "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
            "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
            "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson",
            "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price", "Alvarez"
    };
    private static final String[] PHONE_NUMBER_PATTERNS = { "############" };
    private static final String[] VIRTUAL_ACCOUNT_PATTERNS = { "### ######" };

    private static final String[] STREET_NAMES = { "Main St", "First Ave", "Second St", "Elm St", "Maple Ave", "Oak St",
            "Cedar Ave", "Pine St", "Washington Ave", "Lincoln St" };
    private static final String[] CITIES = { "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
            "San Antonio", "San Diego", "Dallas", "San Jose" };
    private static final String[] STATES = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID",
            "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV",
            "WI", "WY" };
    private static final String[] ZIP_CODES = { "10001", "90001", "60601", "77001", "85001", "19101", "78201", "92101",
            "75201", "95101" };

    private static final String[] BANK_NAMES = {
            "Bank of America",
            "Wells Fargo",
            "JPMorgan Chase",
            "Citibank",
            "HSBC",
            "US Bank",
            "PNC Bank",
            "TD Bank",
            "Capital One",
            "SunTrust",
            "BB&T",
            "Regions Bank",
            "KeyBank",
            "Santander Bank",
            "Fifth Third Bank",
            "M&T Bank",
            "Huntington Bank",
            "Citizens Bank",
            "Union Bank",
            "Comerica Bank"
    };
    private static final String[] BLOOD_TYPE = { "A", "B", "AB", "O" };
    private static final String[] ROLE_TYPE = { "Admin", "Pasien", "Dokter", "Faskes" };
    private static final String[] PAYMENT_METHOD = { "DEBIT / CREDIT CARD", "CASH", "VIRTUAL ACCOUNT", "MART",
            "E-WALLET" };
    private static final String[] CUSTOMER_RELATIONSHIP = { "Ayah", "Ibu", "Anak" };

    private static final String[] EDUCATIONAL_LEVEL = { "SD", "SMP", "SMA/K", "D1", "D2", "D3", "S1/D4", "S2", "S3",
            "Prof" };

    private static final String[] MEDICAL_FACILITY_CATEGORY = { "Apotek", "Rumah Sakit", "Klinik", "Puskesmas" };

    private static final String[] MEDICAL_ITEM_CATEGORY = { "Demam", "Batuk", "Pilek", "Hipertensi" };

    private static final String[] COURIER_NAMES = {
            "ExpressDeliver",
            "SpeedyShip",
            "SwiftCourier",
            "QuickDispatch",
            "RapidRunners",
            "FastTrack Logistics",
            "InstantDelivery",
            "Pegasus Express",
            "TurboTransit",
            "Lightning Logistics"
    };
    private static final String[] COURIER_TYPE = {
            "Instant",
            "Same day",
            "3 days",
            "7 days",
    };

    private static final String[] LOCATION_LEVEL_STRING = {
            "Negara",
            "Provinsi",
            "Kota/Kabupaten",
            "Kecamatan",
            "Kelurahan/Desa",
            "Rukun Warga",
            "Rukun Tangga",
    };
    private static final String[] LOCATION_LEVEL_ABR_STRING = {
            "Negara",
            "Prov",
            "Kota/Kab",
            "Kec",
            "Kel",
            "RW",
            "RT",
    };

    private static final String[] LOCATION_STRING = {};

    private static final Integer[] E_WALLET_NOMINAL_STRING = {
            50000,
            100000,
            150000,
            200000,
            250000,
            300000,
            500000,
    };

    private static final String[] MEDICAL_ITEM_SEGMENTATIONS = {
            "Jamu",
            "Obat Herbal Standar",
            "Obat Bebas (hijau)",
            "Obat Bebas (kuning)",
            "Obat Keras (merah)",
    };
    private static final String[] SPECIALIZATION_STRING = {
            "Umum",
            "Spesialis Anak",
            "Spesialis Kulit",
            "Spesialis Gigi",
            "Spesialis THT",
            "Spesialis Jantung",
            "Spesialis Penyakit Dalam",
            "Spesialis Jiwa",
    };

    private static final String[] INSTITUTION_NAMES = {
            "Institution of Health Sciences",
            "Institute of Medical Research",
            "Academy of Clinical Medicine",
            "Center for Advanced Healthcare",
            "College of Physicians and Surgeons",
            "School of Dentistry and Oral Health",
            "Hospital University Medical Center",
            "Medical Institute of Technology",
            "Institute of Biomedical Sciences",
            "Center for Medical Education and Research",
            "National Institute of Health",
            "Medical College and Hospital",
            "Institute of Pharmaceutical Sciences",
            "Research Institute of Neurology",
            "School of Nursing and Midwifery",
            "Institute of Public Health",
            "Center for Rehabilitation Medicine",
            "College of Veterinary Medicine",
            "Institute of Alternative Medicine",
            "Center for Medical Ethics and Human Rights"
    };
    private static final String[] MEDICAL_MAJORS = {
            "Medicine",
            "Surgery",
            "Pediatrics",
            "Obstetrics and Gynecology",
            "Internal Medicine",
            "Psychiatry",
            "Anesthesiology",
            "Radiology",
            "Pathology",
            "Dermatology",
            "Orthopedics",
            "Ophthalmology",
            "Neurology",
            "Cardiology",
            "Gastroenterology",
            "Oncology",
            "Nephrology",
            "Pulmonology",
            "Infectious Diseases",
            "Emergency Medicine"
    };

    private static final String[] DOCTOR_TREATMENTS = {
            "Laboratory tests and analysis",
            "Physical therapy",
            "Counselling and therapy sessions",
            "Vaccinations and immunizations",
            "Radiological imaging and interpretation",
            "Rehabilitation programs",
            "Lifestyle and dietary recommendations",
            "Chronic disease management",
            "Preventive care and screenings",
            "Pain management techniques",
            "Wound care and dressing changes",
            "Casting and splinting",
            "Referrals to specialists",
            "Monitoring and managing chronic conditions",
            "Emergency medical interventions",
            "Obstetric and gynecological care",
            "Palliative and end-of-life care",
            "Nutritional counseling",
            "Allergy testing and treatment",
            "Dental procedures and oral care"
    };

    private static final String[] PLACE_NAME_PREFIXES = {
            "New",
            "Old",
            "East",
            "West",
            "North",
            "South",
            "Upper",
            "Lower",
            "Grand",
            "Little",
            "Royal",
            "Hidden",
            "Misty",
            "Sunny",
            "Crystal",
            "Whispering",
            "Golden",
            "Silver",
            "Emerald",
            "Diamond"
    };

    private static final String[] PLACE_NAME_SUFFIXES = {
            "City",
            "Town",
            "Village",
            "Hamlet",
            "Meadows",
            "Hills",
            "Valley",
            "Grove",
            "Harbor",
            "Beach",
            "Island",
            "Forest",
            "Lake",
            "River",
            "Cove",
            "Garden",
            "Mansion",
            "Manor",
            "Castle",
            "Lighthouse"
    };

    private static final String[] FACILITY_PREFIXES = {
            "Advanced",
            "Elite",
            "Central",
            "Global",
            "Specialty",
            "Integrated",
            "Modern",
            "Progressive",
            "Quality",
            "Prime",
            "Innovative",
            "Total",
            "Precision",
            "Optimal",
            "Wellness",
            "Complete",
            "Comprehensive",
            "Superior",
            "Primary",
            "Holistic"
    };

    private static final String[] FACILITY_SUFFIXES = {
            "Medical Center",
            "Hospital",
            "Clinic",
            "Healthcare",
            "Care Facility",
            "Health Center",
            "Medical Group",
            "Wellness Center",
            "Medical Clinic",
            "Health Clinic",
            "Medical Institute",
            "Health Institute",
            "Medical Services",
            "Health Services",
            "Medical Care",
            "Healthcare Services",
            "Hospital Group",
            "Medical Practice",
            "Health Practice",
            "Medical Associates"
    };

    private static final String[] DRUG_PREFIXES = {
            "Acetaminophen",
            "Ibuprofen",
            "Aspirin",
            "Amoxicillin",
            "Lisinopril",
            "Simvastatin",
            "Metformin",
            "Atorvastatin",
            "Omeprazole",
            "Metoprolol",
            "Losartan",
            "Albuterol",
            "Hydrochlorothiazide",
            "Levothyroxine",
            "Gabapentin",
            "Amlodipine",
            "Azithromycin",
            "Prednisone",
            "Escitalopram",
            "Trazodone"
    };

    private static final String[] DRUG_SUFFIXES = {
            "Tablets",
            "Capsules",
            "Injection",
            "Syrup",
            "Ointment",
            "Cream",
            "Drops",
            "Inhaler",
            "Patches",
            "Gel",
            "Lotion",
            "Solution",
            "Suppositories",
            "Suspension",
            "Granules",
            "Powder",
            "Nasal Spray",
            "Mouthwash",
            "Eye Drops",
            "Ear Drops"
    };

    private static final String[] COMPOSITION_PREFIXES = {
            "Acetyl",
            "Benzyl",
            "Hydroxy",
            "Methyl",
            "Ethyl",
            "Propyl",
            "Isopropyl",
            "Butyl",
            "Isobutyl",
            "Glycol",
            "Phenyl",
            "Furoate",
            "Acetate",
            "Chloride",
            "Sulfate",
            "Carbonate",
            "Nitrate",
            "Phosphate",
            "Bromide",
            "Iodide"
    };

    private static final String[] COMPOSITION_SUFFIXES = {
            "chloride",
            "acetate",
            "sulfate",
            "carbonate",
            "nitrate",
            "phosphate",
            "bromide",
            "iodide",
            "hydroxide",
            "citrate",
            "gluconate",
            "malate",
            "succinate",
            "tartrate",
            "fumarate",
            "mesylate",
            "benzoate",
            "salicylate",
            "glycolate",
            "lactate"
    };

    private static final String[] MANUFACTURE_PREFIXES = {
            "Astra",
            "Bio",
            "Cura",
            "Delta",
            "Evo",
            "Flex",
            "Geno",
            "Halo",
            "Inno",
            "Juno",
            "Keto",
            "Luma",
            "Medi",
            "Nexo",
            "Omega",
            "Penta",
            "Quanta",
            "Rexo",
            "Sano",
            "Terra"
    };

    private static final String[] MANUFACTURE_SUFFIXES = {
            "Pharma",
            "Med",
            "Health",
            "Care",
            "Bio",
            "Lab",
            "Tech",
            "Solutions",
            "Pharmaceuticals",
            "Innovations",
            "Labs",
            "Medical",
            "Research",
            "Biotech",
            "Science",
            "Therapeutics",
            "Industries",
            "Inc",
            "Ltd",
            "Corporation"
    };

    private static final String[] DOSAGE_UNITS = {
            "mg",
            "g",
            "mcg",
            "mL",
            "IU",
            "tablets",
            "capsules",
            "drops",
            "inhalations",
            "puffs"
    };

    private static final String[] DIRECTION_VERBS = {
            "Take",
            "Use",
            "Apply",
            "Inhale",
            "Administer",
            "Inject",
            "Consume",
            "Spray",
            "Swallow",
            "Chew"
    };

    private static final String[] DIRECTION_NOUNS = {
            "once daily",
            "twice daily",
            "three times daily",
            "every 4 hours",
            "every 6 hours",
            "as needed",
            "before meals",
            "after meals",
            "at bedtime",
            "in the morning",
            "in the evening",
            "with food",
            "with water",
            "under the tongue",
            "on the affected area",
            "to the affected eye",
            "to the affected ear"
    };

    private static final String[] INDICATION_OPTIONS = {
            "for the treatment of",
            "for the management of",
            "to relieve",
            "to reduce",
            "to prevent",
            "to control",
            "to alleviate"
    };

    private static final String[] INDICATION_CONDITIONS = {
            "pain",
            "inflammation",
            "fever",
            "infection",
            "anxiety",
            "depression",
            "hypertension",
            "hyperlipidemia",
            "diabetes",
            "insomnia",
            "allergies",
            "asthma",
            "gastroesophageal reflux",
            "stomach ulcers",
            "nausea",
            "vomiting",
            "diarrhea",
            "constipation"
    };

    private static final String[] CONSTRA_INDICATION_OPTIONS = {
            "Do not use",
            "Avoid using",
            "Not recommended for",
            "Contraindicated in",
            "Do not administer to",
            "Not suitable for"
    };

    private static final String[] CONSTRA_INDICATION_CONDITIONS = {
            "patients with known allergy to the drug",
            "patients with liver disease",
            "patients with kidney disease",
            "patients with heart disease",
            "patients with bleeding disorders",
            "patients with respiratory conditions",
            "patients with gastrointestinal conditions",
            "pregnant or breastfeeding women",
            "children under the age of 12",
            "elderly patients",
            "patients with a history of drug abuse",
            "patients taking certain medications"
    };

    private static final String[] CAUTION_OPTIONS = {
            "Use with caution",
            "Exercise caution",
            "Take precautions",
            "Be aware of",
            "Use carefully",
            "Consider potential risks when using"
    };

    private static final String[] CAUTION_CONDITIONS = {
            "patients with a history of allergies",
            "patients with a history of asthma",
            "patients with a history of seizures",
            "patients with a history of mental health disorders",
            "patients with a history of gastrointestinal disorders",
            "patients with a history of cardiovascular disorders",
            "patients with a history of renal impairment",
            "patients with a history of hepatic impairment",
            "patients with a history of diabetes",
            "patients with a history of thyroid disorders",
            "patients with a history of glaucoma",
            "patients with a history of urinary retention"
    };

    private static final int RANDOM_STRING_LENGTH = 8; // Adjust the length of the random string as desired

    private static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%&*()_-+=<>?/";

    private static final String ALL_CHARS = UPPER_CASE_CHARS + LOWER_CASE_CHARS + DIGITS + SYMBOLS;

    private static final int PASSWORD_LENGTH = 8;

    public String generateDummyEmail() {
        String randomString = generateRandomString(RANDOM_STRING_LENGTH);
        String domain = DOMAINS[new Random().nextInt(DOMAINS.length)];

        return randomString + "@" + domain;
    }

    private static String generateRandomString(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public String generateDummyPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Add at least one upper-case character
        password.append(UPPER_CASE_CHARS.charAt(random.nextInt(UPPER_CASE_CHARS.length())));

        // Add at least one lower-case character
        password.append(LOWER_CASE_CHARS.charAt(random.nextInt(LOWER_CASE_CHARS.length())));

        // Add at least one digit
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        // Add at least one symbol
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));

        // Add the remaining characters to reach the desired length
        for (int i = password.length(); i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return password.toString();
    }

    // Method to generate a random full name
    public String generateFullName() {
        Random random = new Random();
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    // Method to generate a random phone number
    public String generatePhoneNumber() {
        Random random = new Random();
        String pattern = PHONE_NUMBER_PATTERNS[random.nextInt(PHONE_NUMBER_PATTERNS.length)];
        StringBuilder phoneNumber = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '#') {
                if (i == 0) {
                    phoneNumber.append(0);
                    continue;
                }
                if (i == 1) {
                    while (true) {
                        int number = random.nextInt(10);
                        if (number != 0) {
                            phoneNumber.append(number);
                            break;
                        }
                    }
                    continue;
                }

                phoneNumber.append(random.nextInt(10));
                continue;
            }
            phoneNumber.append(c);
        }
        return phoneNumber.toString();
    }

    public String generateFaxNumber() {
        Random random = new Random();

        // Generate random area code (3 digits)
        int areaCode = random.nextInt(900) + 100;

        // Generate random local number (7 digits)
        int localNumber = random.nextInt(9000000) + 1000000;

        // Format the fax number
        return String.format(generateCountryCode() + "-%03d%07d", areaCode, localNumber);
    }

    public String generateCountryCode() {
        Random random = new Random();

        // Generate random country code (1 to 3 digits)
        int countryCode = random.nextInt(999) + 1;

        // Format the country code
        return "+" + countryCode;
    }

    public String generateVirtualAccount() {
        Random random = new Random();
        String pattern = VIRTUAL_ACCOUNT_PATTERNS[random.nextInt(VIRTUAL_ACCOUNT_PATTERNS.length)];
        StringBuilder phoneNumber = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '#') {
                phoneNumber.append(random.nextInt(10));
            } else {
                phoneNumber.append(c);
            }
        }
        return phoneNumber.toString();
    }

    public String[] getBankNames() {
        return BANK_NAMES;
    }

    public String[] getBloodStrings() {
        return BLOOD_TYPE;
    }

    public String[] getPaymentMethods() {
        return PAYMENT_METHOD;
    }

    public String[] getCustomerRelationships() {
        return CUSTOMER_RELATIONSHIP;
    }

    public String[] getEducationalLevel() {
        return EDUCATIONAL_LEVEL;
    }

    public String[] getMedicalItemCategory() {
        return MEDICAL_ITEM_CATEGORY;
    }

    public String[] getMedicalFacilityCategory() {
        return MEDICAL_FACILITY_CATEGORY;
    }

    public String[] getRoleCodeStrings() {
        List<String> list = new ArrayList<String>();
        for (String s : ROLE_TYPE) {
            list.add("ROLE_" + s.toUpperCase());
        }
        return list.toArray(new String[0]);
    }

    public String[] getRoleNameStrings() {
        List<String> list = new ArrayList<String>();
        for (String s : ROLE_TYPE) {
            list.add(s.toLowerCase());
        }
        return list.toArray(new String[0]);
    }

    public String generateAddress() {
        Random random = new Random();
        String street = generateStreet(random);
        String city = CITIES[random.nextInt(CITIES.length)];
        String state = STATES[random.nextInt(STATES.length)];

        return street + ", " + city + ", " + state;

    }

    public String generateZipCode() {
        Random random = new Random();
        return ZIP_CODES[random.nextInt(ZIP_CODES.length)];
    }

    // Method to generate a random street name
    private static String generateStreet(Random random) {
        String streetNumber = String.valueOf(random.nextInt(1000)); // Random street number between 0 and 999
        String streetName = STREET_NAMES[random.nextInt(STREET_NAMES.length)];
        return streetNumber + " " + streetName;
    }

    public String[] getCourierStrings() {
        return COURIER_NAMES;
    }

    public String[] getCourierTypeStrings() {
        return COURIER_TYPE;
    }

    public String[] getLocationLevelStrings() {
        return LOCATION_LEVEL_STRING;
    }

    public String[] getLocationLevelAbrStrings() {
        return LOCATION_LEVEL_ABR_STRING;
    }

    public Integer[] getEWalletNominalString() {
        return E_WALLET_NOMINAL_STRING;
    }

    public String[] getMedicalItemSegmentations() {
        return MEDICAL_ITEM_SEGMENTATIONS;
    }

    public String[] getSpecializationStrings() {
        return SPECIALIZATION_STRING;
    }

    public String[] getDoctorTreatments() {
        return DOCTOR_TREATMENTS;
    }

    public String[] getLocationStrings() {
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(WILAYAH_FILE_PATH))) {
            List<String> stringList = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("','")) {
                    stringList.add(line);
                }
            }

            return stringList.toArray(new String[0]);
        } catch (IOException exception) {
            log.error("ReadWriteFile > readStringFromFile > error ", exception);
            return LOCATION_STRING;
        }
    }

    public String[] generateSTR(int count) {
        List<String> strList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String str = "STR-" + getRandomNumber(1000, 9999) + "-" +
                    getRandomNumber(1000, 9999) + "-" +
                    getRandomNumber(1000, 9999);
            strList.add(str);
        }

        return strList.toArray(new String[0]);
    }

    public String getInstitutionName() {
        String[] institutionNames = INSTITUTION_NAMES;
        return institutionNames[getRandomNumber(0, institutionNames.length - 1)];
    }

    public String getMedicalMajor() {
        String[] major = MEDICAL_MAJORS;
        return major[getRandomNumber(0, major.length - 1)];
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public long getRandomNumberLong(long min, long max) {
        Random random = new Random();
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

    public String getRandomPlaceName() {
        Random random = new Random();
        String prefix = PLACE_NAME_PREFIXES[random.nextInt(PLACE_NAME_PREFIXES.length)];
        String suffix = PLACE_NAME_SUFFIXES[random.nextInt(PLACE_NAME_SUFFIXES.length)];

        return prefix + " " + suffix;
    }

    public String generateFacilityName() {
        Random random = new Random();

        String prefix = FACILITY_PREFIXES[random.nextInt(FACILITY_PREFIXES.length)];
        String suffix = FACILITY_SUFFIXES[random.nextInt(FACILITY_SUFFIXES.length)];

        return prefix + " " + suffix;
    }

    public String generateDay() {
        String[] days = { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };

        return days[getRandomNumber(0, days.length - 2)];
    }

    public LocalTime generateTime(int startHourInt, int endHourInt) {
        int startHour = getRandomNumber(startHourInt, endHourInt);
        int startMinute = getRandomNumber(0, 59);

        return LocalTime.of(startHour, startMinute);
    }

    public String formatLocalTime(LocalTime localTime) {
        return String.format("%02d:%02d", localTime.getHour(), localTime.getMinute());
    }

    public String generateMedicalDrugName() {
        Random random = new Random();

        String prefix = DRUG_PREFIXES[random.nextInt(DRUG_PREFIXES.length)];
        String suffix = DRUG_SUFFIXES[random.nextInt(DRUG_SUFFIXES.length)];

        return prefix + " " + suffix;
    }

    public String generateCompositionName() {
        Random random = new Random();

        String prefix = COMPOSITION_PREFIXES[random.nextInt(COMPOSITION_PREFIXES.length)];
        String suffix = COMPOSITION_SUFFIXES[random.nextInt(COMPOSITION_SUFFIXES.length)];

        return prefix + suffix;
    }

    public String generateManufacturerName() {
        Random random = new Random();

        String prefix = MANUFACTURE_PREFIXES[random.nextInt(MANUFACTURE_PREFIXES.length)];
        String suffix = MANUFACTURE_SUFFIXES[random.nextInt(MANUFACTURE_SUFFIXES.length)];

        return prefix + " " + suffix;
    }

    public String generateDrugDosage() {
        Random random = new Random();

        int dosageValue = random.nextInt(1000) + 1; // Generate dosage value between 1 and 1000
        String dosageUnit = DOSAGE_UNITS[random.nextInt(DOSAGE_UNITS.length)];

        return dosageValue + " " + dosageUnit;
    }

    public String generateDrugDirection() {
        Random random = new Random();

        String verb = DIRECTION_VERBS[random.nextInt(DIRECTION_VERBS.length)];
        String noun = DIRECTION_NOUNS[random.nextInt(DIRECTION_NOUNS.length)];

        return verb + " " + noun;
    }

    public String generateDrugIndication() {
        Random random = new Random();

        String option = INDICATION_OPTIONS[random.nextInt(INDICATION_OPTIONS.length)];
        String condition = INDICATION_CONDITIONS[random.nextInt(INDICATION_CONDITIONS.length)];

        return option + " " + condition;
    }

    public String generateDrugContraindication() {
        Random random = new Random();

        String option = CONSTRA_INDICATION_OPTIONS[random.nextInt(CONSTRA_INDICATION_OPTIONS.length)];
        String condition = CONSTRA_INDICATION_CONDITIONS[random.nextInt(CONSTRA_INDICATION_CONDITIONS.length)];

        return option + " for " + condition;
    }

    public String generateDrugCaution() {
        Random random = new Random();

        String option = CAUTION_OPTIONS[random.nextInt(CAUTION_OPTIONS.length)];
        String condition = CAUTION_CONDITIONS[random.nextInt(CAUTION_CONDITIONS.length)];

        return option + " in " + condition;
    }

    public String getRandomGender() {
        String[] strings = { "F", "M" };
        int index = getRandomNumber(0, 1);
        return strings[index];
    }

    public String getRandomRhesusType() {
        String[] strings = { "+", "-" };
        int index = getRandomNumber(0, 1);
        return strings[index];
    }
}
